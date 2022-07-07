export const environment = {
    production: false,
$if(data.DeployTarget)$
    serviceUrl: "$data.DeployTarget.ServicesURL$"
$else$
    serviceUrl: ""
$endif$
};