package com.loader.lib.loader.extra;

public interface IntentParam {

  interface Service{
    String FROM = "extra_from";
    String CLASS = "extra_class";
    String ACTION = "extra_action";
    String SERVICE_ID = "extra_service_id";
  }

  interface Activity{
    String FROM = "extra_from";

    int FROM_LOADER = 0;
    int FROM_PLUGIN = 1;

    String EXTRA_CLASS = "extra_class";
  }

}
