{
    "name": "$data.PackageName$",
    "version": "1.0.0",
    "description": "QuickStart package.json for the $data.Title$ app",
    "scripts": {
$if(data.CommandDependencies)$
        "prestart": "$data.CommandDependencies:{dep|$dep.Command$};separator=" && "$" ,
        "prebuild": "npm run prestart",
$if(!data.MinimalDependencies)$    
$if(data.TestMode)$
        "preci": "npm run prestart",
        "pree2e": "npm run prestart",
$endif$ 
        "prelighthouse": "npm run prestart",
        "prepwa": "npm run prestart",
$endif$
$if(data.DeployTarget)$
        "predeploy": "npm run prestart",
$endif$
$endif$
        "start": "ng serve -o --port 0",
        "build": "ng build --configuration production",
$if(!data.MinimalDependencies)$    
        "analyze": "webpack-bundle-analyzer dist/stats-es2015.json",
        "build:stats": "ng build --stats-json",
        "serve:test": "ng build && lite-server -c=test/test-bs-config.js",
$if(data.TestMode)$
        "ci": "ng e2e",
        "e2e": "ng e2e",
$endif$
        "lighthouse-inspector": "lighthouse-inspector --config ./test/lighthouse-inspector/config.json",
        "lighthouse": "concurrently --kill-others --success first \"npm run serve:test\" \"wait-on http://localhost:3030 && npm run lighthouse-inspector\"",
        "serve-pwa": "lite-server -c=test/pwa-bs-config.js",
        "pwa": "npm run build && npm run serve-pwa",
$endif$
$if(data.DeployTarget)$
   $if(data.DeployTarget.IsD2C)$
        "deploy": "npm run d2c-prepare && npm run deploy-start && npm run d2c-confirm $data.DeployTarget.Name$",
        "deploy-start": "env-cmd -f ./private/deploy-config.env --silent ng deploy$if(!data.DistributionMode)$ --configuration dev$endif$",
        "d2c-prepare": "d2c-prepare",
        "d2c-confirm": "d2c-confirm",
   $else$
   $if(data.DeployTarget.IsAWSS3)$
        "deploy": "ng deploy$if(!data.DistributionMode)$ --configuration deploy-dev$endif$",
   $else$
   $if(data.DeployTarget.IsLocal)$
        "deploy": "ng build --no-progress$if(!data.DistributionMode)$ --configuration deploy-dev$endif$",
   $endif$
   $endif$
   $endif$
$endif$
        "lint": "eslint ./src/**/*.ts"
    },
    "keywords": [],
    "author": "",
    "license": "MIT",   
    "dependencies": {
        "@angular/animations": "~13.3.0",
        "@angular/common": "~13.3.0",
        "@angular/compiler": "~13.3.0",
        "@angular/core": "~13.3.0",
        "@angular/forms": "~13.3.0",
        "@angular/platform-browser": "~13.3.0",
        "@angular/platform-browser-dynamic": "~13.3.0",
        "@angular/router": "~13.3.0",
        "@angular/service-worker": "~13.3.0",
        "@genexus/ngx-virtual-scroller": "~13.1.1",
        "@genexus/web-standard-functions": "~0.33.0",
        "class-transformer": "^0.2.3",
        "@genexus/web-controls-library": "~1.3.0",
        "core-js": "^2.4.1",
        "placeholder-loading": "^0.5.0",
        "ngx-cookie-service": "^2.2.0",        
        "rxjs": "^6.3.3",
        "tslib": "^2.3.0",
        "zone.js": "~0.11.4"$if (data.PackageDependencies)$,$endif$
        $data.PackageDependencies: { dep | "$dep.Name$": "$dep.Version$" }; separator=",\r\n"$
    },
    "devDependencies": {
        "@angular-devkit/build-angular": "~13.3.0",
        "@angular/cli": "~13.3.0",
        "@angular/compiler-cli": "~13.3.0",
        "@angular/language-service": "~13.3.0",
        "@types/node": "^12.11.1",
        "@types/swiper" : "5.4.0",
        "canonical-path": "0.0.2",
        "codelyzer": "^6.0.0",
        "rimraf": "^2.5.4",
        "ts-node": "~4.1.0",
        "@typescript-eslint/eslint-plugin": "^5.3.0",
        "@typescript-eslint/parser": "^5.3.0",
        "eslint": "^8.1.0",
        "typescript": "~4.4.3",
$if(!data.MinimalDependencies)$
        "lite-server": "^2.2.2",
        "concurrently": "^3.2.0",
        "http-proxy-middleware": "^1.0.1",
        "@genexus/lighthouse-inspector": "^1.0.3",
        "wait-on": "^2.0.2",
$if(data.TestMode)$
        "cypress": "^9.2.1",
        "@cypress/schematic": "^1.6.0",
        "cypress-audit": "^1.1.0",
        "cypress-img-snapshot": "^1.0.0",
$endif$
        "webpack-bundle-analyzer": "^3.4.1",
$endif$
$if(data.DeployTarget)$
$if(data.DeployTarget.IsAWSS3)$
        "@genexus/ngx-aws-deploy": "latest",
        "@genexus/d2c": "latest",
$endif$
        "env-cmd": "latest",
$endif$
        "uglifyjs-webpack-plugin": "^1.1.5"
    },
    "repository": {}
}
