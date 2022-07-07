export const iconContent = new Map();
const requests = new Map();
export function getSvgContent(url) {
    // see if we already have a request for this url
    let request = requests.get(url);
    if (!request) {
        // we don't already have a request
        request = fetch(url).then(response => {
            if (response.ok) {
                return response.text().then(svgContent => {
                    iconContent.set(url, svgContent);
                    return svgContent;
                });
            }
            iconContent.set(url, "");
        });
        // cache for the same requests
        requests.set(url, request);
    }
    return request;
}
