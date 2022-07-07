function JSTree($)
{
	this.Width;
	this.Height;
	this.RootId;
	this.BackUrl;

	this.show = function()
	{
		///UserCodeRegionStart:[show] (do not remove this comment.)

		$.jstree.defaults.core.themes.dots = false;
		$.jstree.defaults.core.themes.icons = false;
		$.jstree.defaults.core.expand_selected_onload = true;
		$.jstree.defaults.core.themes.responsive = true;
		var currentUrl = location.search.replace(new RegExp("%20", 'g'),"+");
		if (currentUrl)
		{
			currentUrl = currentUrl.replace(/,*$/, '');
			if (this.BackUrl)
			{
				gx.$('a[href$="' + this.BackUrl + '"]').parent().attr('data-jstree','');
			}
			gx.$('a[href$="' + currentUrl + '"]').parent().attr('data-jstree','{"opened":true,"selected":true}');
			this.BackUrl = currentUrl;
		}
		if(this.RootId)
		{
			var jstree_inst = gx.$(this.RootId).jstree({ plugins : ["state"] });
			if (jstree_inst.length > 0) {
				jstree_inst.bind('ready.jstree', function (e, data) {
					var depth = 2;
					var firstA;
					cUrl = location.search.replace(/,*$/, '').replace(new RegExp("%20", 'g'),"+")
					var current = data.instance.get_container().find('a[href$="' + cUrl + '"]');
					if (current.length > 0) {
						current.parent().each(function (i) { data.instance.select_node(this, false, false); });
						
					}
					else {
						data.instance.get_container().find('li').first().each(function (i) {
							if (data.instance.get_path(this).length <= depth){
								data.instance.open_node(this);
							}
						});//each 
					}

					var next = gx.$(".JSTreeNextLink");
					var nextTitle = gx.$(".JSTreeNextTitle");
					if (next){
						var n;
						if (current.length > 0) {
							var curr = data.instance.get_selected(false);
							if(curr){
								n = data.instance.get_next_dom(curr[0]);
								if(n.length > 0) //If there is next node
									firstA = n.find('a[href!="#"]:first');
							}
						} else {
							n = data.instance.get_container_ul();//no selected node, take the first root
							if (n.length > 0)
								firstA = n.find('a[href!="#"]:first');
						}
						if (!firstA || firstA.length == 0)
							firstA = n.find('a:first');
						if (n.length > 0 && firstA && firstA.attr('href')) {
							if (firstA.attr('href') == '#') {
								if (!data.instance.is_open(firstA))
									data.instance.toggle_node(firstA);
								firstA = data.instance.get_next_dom(firstA).find('a[href!="#"]:first');
							}
							next.find('a').text(' ' + firstA.text());
							next.find('a').attr('href', firstA.attr('href'));
							next.show();
							nextTitle.show();
						}
					}
	
					data.instance.get_container().find('a[href$="#"]').each(function (i) {
						var n = data.instance.get_node(this);
						if (data.instance.is_leaf(n)){
							data.instance.disable_node(n);
						}
					});//each


				}).bind("select_node.jstree", function (e, data) {
					if (data.node.a_attr['href']==='#')
						if (data.instance.is_leaf(data.node))
							return false;
						else
							return data.instance.toggle_node(data.node);

					if (data.node.a_attr['target'] == '_blank') {
						window.open(data.node.a_attr['href']);
					}
				}); //bind

			}
		}
		
		///UserCodeRegionEnd: (do not remove this comment.)
	}
	///UserCodeRegionStart:[User Functions] (do not remove this comment.)

	this.destroy = function()
	{
		gx.$(this.RootId).jstree("destroy");
	}
	
	
	
	///UserCodeRegionEnd: (do not remove this comment.):
}
