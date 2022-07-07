Build & Deploy from Genexus sources
---

1. Make sure you've got your `JAVA_HOME` and `ANDROID_HOME` environment variables set.

    _Example_
    ```
    JAVA_HOME=C:\Program Files\Java\jdk
    ANDROID_HOME=C:\AndroidSDK
    ```

2. Then build and deploy your changes by executing the following command from the `Android\Sources`
directory inside your installation (e.g. C:/path/to/Genexus/Android/Sources).

    ```
    gradlew publishDebugPublicationToInternalRepository
    ```

3. Finally, verify that the changes were correctly applied by browsing the `m2Repository` directory:
    ```
    C:/path/to/Genexus/Android/m2Repository/com/genexus/FlexibleClient/*
    ```

If you require additional help visit the [Troubleshooting section](https://wiki.genexus.com/commwiki/servlet/wiki?43511) on GeneXus' wiki.
