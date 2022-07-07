function GXMediaPlayer()
{
	var YOUTUBE_SOURCE_TYPE = 'youtube',
		VIMEO_SOURCE_TYPE = 'vimeo',
		OTHER_SOURCE_TYPE = 'video';
		
	var YOUTUBE_URL_REGEX = /youtube\.com|youtu\.be/,
		VIMEO_URL_REGEX = /vimeo\.com/,
		YOUTUBE_URL_PARSING_REGEX = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/,
		VIMEO_URL_PARSING_REGEX = /https?:\/\/(?:www\.|player\.)?vimeo.com\/(?:channels\/(?:\w+\/)?|groups\/([^\/]*)\/videos\/|album\/(\d+)\/video\/|video\/|)(\d+)(?:$|\/|\?)/;
	
	var compiledTemplates = {},
		templatesSource = {};
		
	templatesSource[YOUTUBE_SOURCE_TYPE] = '<iframe id="{{id}}" width="{{width}}" height="{{height}}" src="{{url}}" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>';
	templatesSource[VIMEO_SOURCE_TYPE] = templatesSource[YOUTUBE_SOURCE_TYPE];
	templatesSource[OTHER_SOURCE_TYPE] = [
		'<video id="{{id}}" class="video-js vjs-default-skin"',
				'controls preload="auto" width="{{width}}" height="{{height}}">',
			'<source src="{{url}}" />',
			'<p class="vjs-no-js"><a href="{{url}}" target="_blank">{{noJsText}}</a></p>',
		'</video>'
	].join("");
	
	var applyTemplate = function (sourceType, context) {
		if (!compiledTemplates[sourceType]) {
			compiledTemplates[sourceType] = true;
			Mustache.parse(templatesSource[sourceType]);
		}
		return Mustache.render(templatesSource[sourceType], context);
	};
	
	var resolveSourceType = function (source) {
		if (source.search(YOUTUBE_URL_REGEX) !== -1)
			return YOUTUBE_SOURCE_TYPE;
		
		if (source.search(VIMEO_URL_REGEX) !== -1)
			return VIMEO_SOURCE_TYPE;
		
		return OTHER_SOURCE_TYPE;
	};
	
	var resolveSourceUrl = function (source, sourceType, autoPlay) {
		var match,
			videoId = "";

		if (sourceType == YOUTUBE_SOURCE_TYPE) {
			match = source.match(YOUTUBE_URL_PARSING_REGEX);
			videoId =  (match && match[2]) ? match[2] : "";
			return "https://www.youtube.com/embed/" + videoId + (autoPlay ? "?autoplay=1" : "");
		}

		if (sourceType == VIMEO_SOURCE_TYPE) {
			match = source.match(VIMEO_URL_PARSING_REGEX);
			videoId =  (match && match[3]) ? match[3] : "";
			return "https://player.vimeo.com/video/" + videoId + (autoPlay ? "?autoplay=1" : "");
		}

		return source;
	};
	
	var id;
	this.show = function () {
		id = this.getContainerControl().id + "_video";

		var sourceType = resolveSourceType(this.Source),
			autoPlay = this.Autoplay === "true";
		
		this.setHtml(applyTemplate(sourceType, {
			id: id,
			url: resolveSourceUrl(this.Source, sourceType, autoPlay),
			width: gx.dom.addUnits(this.Width),
			height: gx.dom.addUnits(this.Height),
			noJsText: this.MediaTitle || "Your media here!"
		}));
		
		if (sourceType == OTHER_SOURCE_TYPE) {
			videojs(id, {
				autoplay: autoPlay
			});
		}
	};
}