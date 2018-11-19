package com.paandw.poke.data.domain;

import com.paandw.poke.data.AppService;

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
