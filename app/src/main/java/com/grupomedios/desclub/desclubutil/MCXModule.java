package com.grupomedios.desclub.desclubutil;

import com.grupomedios.desclub.desclubapi.facade.DiscountFacade;
import com.grupomedios.desclub.desclubutil.security.UserHelper;

import dagger.Module;

@Module(injects = {
        DiscountFacade.class,

        UserHelper.class

})
public class MCXModule {

}
