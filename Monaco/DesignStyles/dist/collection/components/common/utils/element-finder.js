export class ElementFinder {
    static closeElement(baseElement, tagName, id) {
        let candidates = baseElement.getElementsByTagName(tagName);
        for (let i = 0; i < candidates.length; i++) {
            let node = candidates[i];
            if (node.id == id && node.parentNode)
                node.parentNode.removeChild(node);
        }
    }
}
