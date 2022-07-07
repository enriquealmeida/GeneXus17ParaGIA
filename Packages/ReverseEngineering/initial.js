function initializeControls() {
	for(i=0; i<document.forms[0].metadata.lenght ; i++)
	{alert('hola');
		document.forms[0].metadata[i].disabled = true;
	}
	for(i=0; i<document.forms[0].connection.lenght ; i++)
	{
		document.forms[0].connection[i].disabled = true;
	}
}

function mdataRadioClick(clickedRadio) {
	try
	{
		if(clickedRadio.value == "other_mdata")
		{
			document.getElementById("mdataSelFile").disabled = false;
			window.external.SetStartingMechanism(3);
		}
		else
		{
			document.getElementById("mdataSelFile").disabled = true;
			window.external.SetStartingMechanism(4);
		}
	}
	catch (exception) {}
}

function metadataSelected() {
	try
	{ 
		window.external.SetMetadataToOpen(document.getElementById("mdataSelFile").value);
	}
	catch (exception) {}
}