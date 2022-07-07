export const environment = {
    production: true,
$if(data.DeployTarget)$
    serviceUrl: "$data.DeployTarget.ServicesURL$"
$else$
    serviceUrl: ""
$endif$
};