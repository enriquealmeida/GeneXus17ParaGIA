<!DOCTYPE html>
<html>
  <head>
    <title>$data.Title$</title>
    <base href="/" />
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    $if(data.FaviconImage)$<link rel="shortcut icon" href="$data.FaviconImage$"/>$else$<link rel="shortcut icon" href="assets/icons/favicon.png" />$endif$
    $if(data.DefaultThemeId)$<link rel="preload" href="$data.DefaultThemeId$.css" as="style" />$endif$
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#1976d2" />
    $if(data.WebappImage192)$<link rel="apple-touch-icon" href="$data.WebappImage192$"/>$else$<link rel="apple-touch-icon" href="assets/icons/icon-192x192.png" />$endif$
  </head>
  <body class="Application">
    <my-app> </my-app>
    <noscript>Please enable JavaScript to continue using this application.</noscript>
  </body>
</html>
