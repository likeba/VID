package com.nomadconnection.dapp.icon;

import com.nomadconnection.dapp.exception.IconServiceException;
import foundation.icon.icx.*;
import foundation.icon.icx.data.Address;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.transport.jsonrpc.RpcObject;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Score 의 external function 을 호출하는 서비스
 */
public class ScoreService {

    private IconService iconService;
    private BigInteger networkId;

    public ScoreService(IconService iconService, BigInteger networkId) {
        this.iconService = iconService;
        this.networkId = networkId;
    }

    public Bytes transaction(ScoreCall scoreCall, Wallet wallet) throws IconServiceException {
        if (scoreCall.isReadOnly()) {
            throw new IconServiceException("This method is read only function.");
        }
        Transaction tx = buildTransaction(wallet.getAddress(),
                scoreCall.getAddress(),
                scoreCall.getMethod(),
                scoreCall.getParams());
        return sendTransaction(tx, wallet);
    }

    public <T> T call(ScoreCall scoreCall, Class<T> responseType) throws IconServiceException {
        Call<T> c = buildCall(scoreCall.getAddress(),
                scoreCall.getMethod(),
                scoreCall.getParams(),
                responseType);
        try {
            return iconService.call(c).execute();
        } catch (IOException e) {
            throw new IconServiceException(e.getMessage(), e);
        }
    }

    private Transaction buildTransaction(Address from, Address scoreAddress, String method, RpcObject params) {
        long timestamp = System.currentTimeMillis() * 1000L;
        return TransactionBuilder.newBuilder()
                .nid(this.networkId)
                .from(from)
                .to(scoreAddress)
                .stepLimit(new BigInteger("5000000"))
                .timestamp(new BigInteger(Long.toString(timestamp)))
                .call(method)
                .params(params)
                .build();
    }

    private <T> Call<T> buildCall(Address scoreAddress, String method, RpcObject params, Class<T> responseType) {
        return (new foundation.icon.icx.Call.Builder())
                .to(scoreAddress)
                .method(method)
                .params(params)
                .buildWith(responseType);
    }

    private <T> Call<T> buildCall(Address from, Address scoreAddress, String method, RpcObject params, Class<T> responseType) {
        return (new foundation.icon.icx.Call.Builder())
                .from(from)
                .to(scoreAddress)
                .method(method)
                .params(params)
                .buildWith(responseType);
    }

    /**
     * Sends a transaction.
     *
     * @param transaction the Transaction object
     * @param wallet      the wallet for transaction
     * @return the hash of transaction
     * @see IconService#sendTransaction(SignedTransaction)
     */
    protected Bytes sendTransaction(Transaction transaction, Wallet wallet) throws IconServiceException {
        try {
            SignedTransaction signedTransaction = new SignedTransaction(transaction, wallet);
            return iconService.sendTransaction(signedTransaction).execute();
        } catch (IOException e) {
            throw new IconServiceException(e.getMessage(), e);
        }
    }

}
