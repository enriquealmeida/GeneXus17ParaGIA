
//$curl.Source$

$if (curl.HasAuth)$
&HttpClient.AddAuthentication($curl.AuthMethod$, !"$curl.AuthRealm$", !"$curl.AuthUser$", !"$curl.AuthPassword$")
$endif$
$if (curl.HasProxy)$
&HttpClient.AddProxyAuthentication($curl.AuthMethod$, "", !"$curl.ProxyUser$", !"$curl.ProxyPassword$")
$endif$
$curl.Variables:{ v | 
&HttpClient.AddVariable(!"$v$", !"$curl.Variables.(v)$")
}$
$curl.Headers:{ h | 
&HttpClient.AddHeader(!"$h$", !"$curl.Headers.(h)$")
}$
$if (curl.HasFile)$
&HttpClient.AddFile(!"$curl.File$")
$endif$
$if (curl.HasBody)$
&HttpClient.AddString(!$curl.Body$)
$endif$

&HttpClient.Execute(!"$curl.HttpMethod$", !"$curl.Url$")

