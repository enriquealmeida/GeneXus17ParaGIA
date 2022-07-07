{
  "images": [
    $data.Images:{ img | { 
      "name": "$img.Name$", 
      "theme": "$img.Theme$", 
      "lang": "$img.Language$",
      "colorScheme": "$img.ColorScheme$",
      "options": [
        $img.Options.keys: {key | {"name":"$key$", "value":"$img.Options.(key)$"\}};separator = ",\n"$
      ],
      "density": [$img.Density:{ density | "$density$"};separator = ", "$], 
      "location": "$img.Uri$"
\}};separator = ",\r\n"$
  ]
}