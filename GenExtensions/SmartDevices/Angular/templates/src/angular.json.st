{
  "\$schema": "./node_modules/@angular/cli/lib/config/schema.json",
  "cli": {
    "analytics": false
  },
  "version": 1,
  "newProjectRoot": "projects",
  "projects": {
    "genexus-web-application": {
      "root": "",
      "sourceRoot": "src",
      "projectType": "application",
      "architect": {
        "build": {
          "builder": "@angular-devkit/build-angular:browser",
          "options": {
            "aot": true,
            "outputPath": "dist",
            "index": "src/index.html",
            "main": "src/main.ts",
            "tsConfig": "src/tsconfig.app.json",
            "polyfills": "src/polyfills.ts",
            "assets": [
              "src/sass",
              "src/translations",
              "src/manifest.webmanifest",
              "src/assets",
              {
                "glob": "**/*",
                "input": "./node_modules/@genexus/web-controls-library/dist/gx-web-controls/assets",
                "output": "/assets/"
              }
            ],
            "styles": [
              $data.Themes:{ thm |{"input": "src/sass/$thm.ProjectPath$.scss", "inject": $if(thm.IsDefault)$true$else$false$endif$, "bundleName": "$thm.Id$"\}};separator = ",\r\n"$$if (data.UserControlsGlobalStyles)$,$endif$
              $data.UserControlsGlobalStyles : { style | "$style$"};separator = ",\r\n"$
            ],
            "scripts": [],
            "allowedCommonJsDependencies": [
              "class-transformer"
            ],
            "vendorChunk": true,
            "extractLicenses": false,
            "buildOptimizer": false,
            "sourceMap": true,
            "optimization": false,
            "namedChunks": true
          },
          "configurations": {
            "production": {
                "budgets": [
                  {
                    "type": "anyComponentStyle",
                    "maximumWarning": "6kb"
                  }
                ],
                "fileReplacements": [
                    {
                        "replace": "src/environments/environment.ts",
                        "with": "src/environments/environment.prod.ts"
                    }
                ],
                "optimization": true,
                "outputHashing": "all",
                "sourceMap": false,
                "namedChunks": false,
                "aot": true,
                "extractLicenses": true,
                "vendorChunk": false,
                "buildOptimizer": true,
                "serviceWorker": true,
    $if(data.DeployTarget)$
                "baseHref":"$data.DeployTarget.BaseHref$",
    $endif$
                "ngswConfigPath": "ngsw-config.json"
            }
    $if(data.DeployTarget)$
          ,"deploy-dev": {
                "baseHref":"$data.DeployTarget.BaseHref$",
                "fileReplacements": [
                    {
                        "replace": "src/environments/environment.ts",
                        "with": "src/environments/environment.dev.ts"
                    }
                ]
            }
    $endif$
         }
        },
        "serve": {
          "builder": "@angular-devkit/build-angular:dev-server",
          "options": {
            $if(data.DeveloperMode)$
            "proxyConfig": "proxy.conf.json",
            $endif$
            "browserTarget": "genexus-web-application:build"
          },
          "configurations": {
            "prod": {
              "browserTarget": "genexus-web-application:build:prod"
            },
            "production": {
              "browserTarget": "genexus-web-application:build:production"
            }
          }
        },
$if(data.TestMode && !data.MinimalDependencies)$
        "e2e": {
          "builder": "@cypress/schematic:cypress",
          "options": {
            "devServerTarget": "genexus-web-application:serve",
            "browser": "chrome",
            "headless": true,
            "watch": false,
            "configFile": "test/cypress/cypress.e2e.json"
          },
          "configurations": {
            "production": {
              "devServerTarget": "genexus-web-application:serve:production"
            }
          }
        },
         "cypress-open": {
          "builder": "@cypress/schematic:cypress",
          "options": {
            "devServerTarget": "genexus-web-application:serve",
            "watch": true,
            "headless": false,
            "browser": "chrome",
            "configFile": "test/cypress/cypress.e2e.json"
          }
        },
$endif$
        "extract-i18n": {
          "builder": "@angular-devkit/build-angular:extract-i18n",
          "options": {
            "browserTarget": "genexus-web-application:build"
          }
        },
        "lint": {
          "builder": "@angular-devkit/build-angular:eslint",
          "options": {
            "tsConfig": [
              "src/tsconfig.app.json",
              "src/tsconfig.spec.json"
            ],
            "exclude": [
              "**/node_modules/**"
            ]
          }
        },
        "deploy": {
        $if(data.DeployTarget)$
        $if(data.DeployTarget.IsAWSS3)$
          "builder": "@genexus/ngx-aws-deploy:deploy",
          "options": {
            "buildTarget": "genexus-web-application:build:production",
            "subFolder": "$data.DeployTarget.FolderName$"
          },
          "configurations": {
            "production": {
              "buildTarget": "genexus-web-application:build:production",
              "url": "$data.DeployTarget.URL$"
            },
            "dev": {
              "buildTarget": "genexus-web-application:build:deploy-dev",
              "url": "$data.DeployTarget.URL$"
            }
          }
          $endif$
          $endif$
        }
      }
    },
    "genexus-web-application-e2e": {
      "root": "e2e",
      "sourceRoot": "e2e",
      "projectType": "application"
    }
  },
  "defaultProject": "genexus-web-application",
  "schematics": {
    "@schematics/angular:component": {
      "prefix": "app",
      "style": "scss"
    },
    "@schematics/angular:directive": {
      "prefix": "app"
    }
  }
}