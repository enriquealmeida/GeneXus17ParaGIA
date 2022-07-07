{
    "/$data.HostRelativeUrl$": {
        "target": "$data.TargetHostUrl$",
        "secure": false,
        "changeOrigin": true,
        "logLevel": "debug",
        "pathRewrite": {
            "^/$data.HostRelativeUrl$": ""
        }
    }
}
