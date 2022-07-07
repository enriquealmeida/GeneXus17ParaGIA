function displayNugget()
{
	alert('Not implemented...');
}

function AttCrossReference(attName)
{
	try
	{ 
		window.external.AttCrossReference(attName);
	}
	catch (exception)
	{}
}