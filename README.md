# movieShop
A project for Udacity "Android Programmer Path"

Before running an app you must include your Movie DB API.

In the gradle.properties file (root directory of the project) add a line:
API_KEY = “your_api_key”

Be shure that the top-level build.gradle file defines the API_KEY property

android{
  ...
  defaultConfig{
    ...
    buildConfigField("String", "API_KEY", API_KEY)
  }
}

Or simply in the utilities folder NetworkUtils.java class change the line
private final static String api = BuildConfig.API_KEY;
to
private final static String api = “your_api_key”;
