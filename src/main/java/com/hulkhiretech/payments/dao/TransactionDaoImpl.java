package com.hulkhiretech.payments.dao;

import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.entity.TransactionEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TransactionDaoImpl implements TransactionDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    ModelMapper mapper;

    TransactionDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, ModelMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO txnDto) {
        log.info("Processing createTransaction of TransactionDaoImpl");
        //convert TransactionDTO to TransactionEntity
        TransactionEntity txnEntity = mapper.map(txnDto, TransactionEntity.class);
        log.info("Converted Entity from dto in createTransaction :{}", txnEntity);
        //Update in db Logic


        String sql = "INSERT INTO `Transaction` (" +
                "userId, paymentMethodId, providerId, paymentTypeId, txnStatusId, " +
                "amount, currency, merchantTxnReference, txnReference, providerReference, " +
                "errorCode, errorMessage, retryCount" +
                ") VALUES (" +
                ":userId, :paymentMethodId, :providerId, :paymentTypeId, :txnStatusId, " +
                ":amount, :currency, :merchantTxnReference, :txnReference, :providerReference, " +
                ":errorCode, :errorMessage, :retryCount" +
                ")";

        KeyHolder keyHolder=new GeneratedKeyHolder();
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(txnEntity);


        try {
            int rows = jdbcTemplate.update(sql, paramSource,keyHolder,new String[]{"id"});
            log.info("Inserted rows: {}", rows);
        } catch (Exception e) {
            log.error("Insert failed", e);
        }
        int id=keyHolder.getKey()!=null ?keyHolder.getKey().intValue():-1;
        txnDto.setId(id);

        return txnDto;
    }

    @Override
    public TransactionDTO getTransactionByTxnRef(String txnRefs) {
        log.info("Going to JDBC Call to getTransaction by txnRef :{}",txnRefs);

        String sql = "SELECT * FROM `Transaction` WHERE txnReference = :txnReference";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("txnReference", txnRefs);

        try {
            TransactionEntity transactionEntity= jdbcTemplate.queryForObject(sql, paramSource,
                    new BeanPropertyRowMapper<>(TransactionEntity.class));
            log.info("got TransactionEntity back from DB :{}",transactionEntity);

           return mapper.map(transactionEntity,TransactionDTO.class);

        } catch (EmptyResultDataAccessException e) {
            log.warn("No transaction found with txnReference: {}", txnRefs);
            return null;
        }



    }

    @Override
    public TransactionDTO updateTransactionStatusDetails(TransactionDTO txnDTO) {
        log.info("processing updateTransactionStatusDetails  ");

        String sql = "UPDATE payments.`Transaction` SET " +
                "txnStatusId = :txnStatusId, " +
                "providerReference = :providerReference, " +
                "errorCode = :errorCode, " +
                "errorMessage = :errorMessage " +
                "WHERE txnReference = :txnReference";



        TransactionEntity transactionEntity=mapper.map(txnDTO,TransactionEntity.class);
        SqlParameterSource params=new BeanPropertySqlParameterSource(transactionEntity);

        jdbcTemplate.update(sql,params);
        log.info("Txn details updated in DB ||txnDTO :{}",txnDTO);

        return txnDTO;
    }

    @Override
    public TransactionDTO getTransactionByProviderRef(String providerRef) {
        log.info("Going to JDBC Call to getTransaction by txnRef :{}",providerRef);

        String sql = "SELECT * FROM `Transaction` WHERE providerReference = :providerReference";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("providerReference", providerRef);

        try {
            TransactionEntity transactionEntity= jdbcTemplate.queryForObject(sql, paramSource,
                    new BeanPropertyRowMapper<>(TransactionEntity.class));
            log.info("got TransactionEntity back from DB :{}",transactionEntity);

            return mapper.map(transactionEntity,TransactionDTO.class);

        } catch (EmptyResultDataAccessException e) {
            log.warn("No transaction found with providerReference: {}", providerRef);
            return null;
        }


    }
}
