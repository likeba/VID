package com.nomadconnection.dapp;

import com.nomadconnection.dapp.exception.IconServiceException;
import com.nomadconnection.dapp.icon.IconServiceConfig;
import com.nomadconnection.dapp.icon.ScoreCall;
import com.nomadconnection.dapp.icon.ScoreService;
import com.nomadconnection.dapp.icon.DataVerificationScore;
import foundation.icon.icx.IconService;
import foundation.icon.icx.Wallet;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.TransactionResult;
import foundation.icon.icx.transport.http.HttpProvider;
import foundation.icon.icx.transport.jsonrpc.RpcError;
import okhttp3.OkHttpClient;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 블록체인 data 저장/검증 서비스
 */
public class DataVerificationService {

    private IconService iconService;
    private ScoreService scoreService;
    private DataVerificationScore score;

    public DataVerificationService(IconServiceConfig config) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .build();

        iconService = new IconService(new HttpProvider(okHttpClient, config.getUrl(), 3));
        scoreService = new ScoreService(iconService, config.getNetworkId());
        score = new DataVerificationScore(config.getScoreAddress());
    }

    /**
     * Score version 조회
     *
     * @return score version
     */
    public String getVersion() throws IconServiceException {
        ScoreCall scoreCall = score.getVersion();
        return scoreService.call(scoreCall, String.class);
    }

    /**
     * 데이터 저장
     *
     * @param key key
     * @param value value
     * @param wallet 사용자 지갑
     * @return transaction hash
     */
    public String putData(String key, String value, Wallet wallet) throws IconServiceException {
        ScoreCall scoreCall = score.putData(key, value);
        Bytes txHash = scoreService.transaction(scoreCall, wallet);
        return txHash.toString();
    }

    /**
     * 데이터 등록 여부 확인
     */
    public boolean hasData(String key) throws IconServiceException {
        ScoreCall scoreCall = score.hasData(key);
        return scoreService.call(scoreCall, Boolean.class);
    }

    /**
     * 데이터 조회
     */
    public String getData(String key) throws IconServiceException {
        ScoreCall scoreCall = score.getData(key);
        return scoreService.call(scoreCall, String.class);
    }

    public TransactionResult getTransactionResult(String hash) throws IconServiceException {
        return getTransactionResult(hash, 15_000);
    }

    public TransactionResult getTransactionResult(String hash, long timeout) throws IconServiceException {
        return getTransactionResult(new Bytes(hash), timeout);
    }

    /**
     * transaction 결과 조회
     * transaction 결과는 block 에 포함되어 commit 된 이후에 조회가 가능하므로
     * 결과 조회될 때까지 1초마다 {@link IconService#getTransactionResult(Bytes)} 을 요청한다.
     *
     * @param hash transaction hash
     * @param timeout the maximum time to wait
     * @return TransactionResult object
     * @see IconService#getTransactionResult(Bytes)
     * @throws IconServiceException if 'Revert' error occurs into block chain or timeout
     */
    public TransactionResult getTransactionResult(Bytes hash, long timeout) throws IconServiceException {
        try {
            final ExecutorService executor = Executors.newCachedThreadPool();
            return executor.submit(() -> {
                TransactionResult result = null;
                while (result == null) {
                    try {
                        Thread.sleep(1000);
                        result = iconService.getTransactionResult(hash).execute();
                        if (result.getStatus().equals(BigInteger.ZERO)) {
                            throw new IconServiceException(result.getFailure().getMessage());
                        }
                    } catch (RpcError e) {
                        // pending
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                return result;
            }).get(timeout, TimeUnit.MILLISECONDS);

        } catch (TimeoutException e) {
            throw new IconServiceException("timeout");
        } catch (Exception e) {
            throw new IconServiceException(e);
        }
    }


}
