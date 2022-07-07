{
    "name": "$data.Title$",
    "short_name": "$data.Title$",
    "theme_color": "#1976d2",
    "background_color": "#fafafa",
    "display": "standalone",
    "scope": "./",
    "start_url": "./",
    "icons": [
        {
            "src": "$if(data.WebappImage192)$$data.WebappImage192$$else$assets/icons/icon-192x192.png$endif$",
            "sizes": "192x192",
            "type": "image/png"
        },
        {
            "src": "$if(data.WebappImage512)$$data.WebappImage512$$else$assets/icons/icon-512x512.png$endif$",
            "sizes": "512x512",
            "type": "image/png"
        }
    ]
}
