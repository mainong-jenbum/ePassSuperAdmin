package com.jenbumapps.core.api;

import com.jenbumapps.core.utility.connect.NetworkService;

public class ApiManager {

    public static EPassApi PASS = NetworkService.get().create(EPassApi.class);
    public static EPassTermApi TERM = NetworkService.get().create(EPassTermApi.class);
    public static GlobalParamApi GLOBAL_PARAM = NetworkService.get().create(GlobalParamApi.class);
    public static FileApi FILE = NetworkService.get().create(FileApi.class);
    public static CityApi CITY = NetworkService.get().create(CityApi.class);
    public static UserApi USER = NetworkService.get().create(UserApi.class);

    public static ViewModelApi VIEW_MODEL = NetworkService.get().create(ViewModelApi.class);

}
