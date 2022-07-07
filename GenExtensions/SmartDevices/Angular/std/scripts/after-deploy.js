"use strict";

const fs = require("fs");
const path = require("path");

const configName = process.argv[2];
const angularConfigPath = path.join(__dirname, "..", "angular.json");

fs.readFile(angularConfigPath, (err, data) => {
    if (err) throw err;
    let json = JSON.parse(data);

    const url =
        json.projects["genexus-web-application"].architect.deploy.configurations[
            configName
        ].url;

    console.log("\x1b[33m", "Angular Application Deployed at: ", "\x1b[0m");
    console.log("\x1b[32m", url, "\x1b[0m");
});
