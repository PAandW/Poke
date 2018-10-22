package com.paandw.pieceofcake.data.domain;

import com.paandw.pieceofcake.data.AppService;

import retrofit2.Retrofit;

public class IngredientDomain extends BaseDomain {

    private AppService appService;

    public IngredientDomain() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.yummly.com/v1/api")
                .build();

        appService = retrofit.create(AppService.class);
    }

}
