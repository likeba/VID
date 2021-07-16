package com.nomadconnection.dapp.icon;


import foundation.icon.icx.data.Address;
import foundation.icon.icx.transport.jsonrpc.RpcObject;
import foundation.icon.icx.transport.jsonrpc.RpcValue;

/**
 * Score interface 에 맞게 요청할 데이터 생성
 * key-value 형태의 데이터를 저장하고 검증?
 */
public class DataVerificationScore {

    private Address address;

    public DataVerificationScore(Address address) {
        this.address = address;
    }

    /**
     * 호출하려는 score address
     *
     * @return score address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Score version 조회
     *
     * @return score 호출 정보를 갖고 있는 ScoreCall
     */
    public ScoreCall getVersion() {
        return new ScoreCall.Builder()
                .address(address)
                .method("getVersion")
                .readOnly(true)
                .build();
    }

    /**
     * 데이터 저장
     *
     * @return score 호출 정보를 갖고 있는 ScoreCall
     */
    public ScoreCall putData(String key, String value) {
        RpcObject params = new RpcObject.Builder()
                .put("_key", new RpcValue(key))
                .put("_value", new RpcValue(value))
                .build();
        return new ScoreCall.Builder()
                .address(address)
                .method("putData")
                .params(params)
                .build();
    }

    /**
     * 데이터가 블록체인에 등록됐는지 확인
     *
     * @return score 호출 정보를 갖고 있는 ScoreCall
     */
    public ScoreCall hasData(String key) {
        RpcObject params = new RpcObject.Builder()
                .put("_key", new RpcValue(key))
                .build();
        return new ScoreCall.Builder()
                .address(address)
                .method("hasData")
                .params(params)
                .build();
    }

    /**
     * 데이터 조회
     */
    public ScoreCall getData(String key) {
        RpcObject params = new RpcObject.Builder()
                .put("_key", new RpcValue(key))
                .build();
        return new ScoreCall.Builder()
                .address(address)
                .method("getData")
                .params(params)
                .build();
    }

    private void validHash(String hash) {
        if (!isValidHex(hash)) {
            throw new IllegalArgumentException("The value is not hex string.");
        }
    }

    private boolean isValidHex(String value) {
        return value.matches("^[0-9a-fA-F]+$");
    }
}
