(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[3],{

/***/ "../node_modules/@genexus/design-tokens-editor/dist/esm-es5/css-shim-6aaf713d-9b13816a.js":
/*!************************************************************************************************!*\
  !*** ../node_modules/@genexus/design-tokens-editor/dist/esm-es5/css-shim-6aaf713d-9b13816a.js ***!
  \************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

eval("/*\nExtremely simple css parser. Intended to be not more than what we need\nand definitely not necessarily correct =).\n*/\n/** @unrestricted */\nvar StyleNode = /** @class */ (function () {\n    function StyleNode() {\n        this.start = 0;\n        this.end = 0;\n        this.previous = null;\n        this.parent = null;\n        this.rules = null;\n        this.parsedCssText = '';\n        this.cssText = '';\n        this.atRule = false;\n        this.type = 0;\n        this.keyframesName = '';\n        this.selector = '';\n        this.parsedSelector = '';\n    }\n    return StyleNode;\n}());\n// given a string of css, return a simple rule tree\n/**\n * @param {string} text\n * @return {StyleNode}\n */\nfunction parse(text) {\n    text = clean(text);\n    return parseCss(lex(text), text);\n}\n// remove stuff we don't care about that may hinder parsing\n/**\n * @param {string} cssText\n * @return {string}\n */\nfunction clean(cssText) {\n    return cssText.replace(RX.comments, '').replace(RX.port, '');\n}\n// super simple {...} lexer that returns a node tree\n/**\n * @param {string} text\n * @return {StyleNode}\n */\nfunction lex(text) {\n    var root = new StyleNode();\n    root['start'] = 0;\n    root['end'] = text.length;\n    var n = root;\n    for (var i = 0, l = text.length; i < l; i++) {\n        if (text[i] === OPEN_BRACE) {\n            if (!n['rules']) {\n                n['rules'] = [];\n            }\n            var p = n;\n            var previous = p['rules'][p['rules'].length - 1] || null;\n            n = new StyleNode();\n            n['start'] = i + 1;\n            n['parent'] = p;\n            n['previous'] = previous;\n            p['rules'].push(n);\n        }\n        else if (text[i] === CLOSE_BRACE) {\n            n['end'] = i + 1;\n            n = n['parent'] || root;\n        }\n    }\n    return root;\n}\n// add selectors/cssText to node tree\n/**\n * @param {StyleNode} node\n * @param {string} text\n * @return {StyleNode}\n */\nfunction parseCss(node, text) {\n    var t = text.substring(node['start'], node['end'] - 1);\n    node['parsedCssText'] = node['cssText'] = t.trim();\n    if (node.parent) {\n        var ss = node.previous ? node.previous['end'] : node.parent['start'];\n        t = text.substring(ss, node['start'] - 1);\n        t = _expandUnicodeEscapes(t);\n        t = t.replace(RX.multipleSpaces, ' ');\n        // TODO(sorvell): ad hoc; make selector include only after last ;\n        // helps with mixin syntax\n        t = t.substring(t.lastIndexOf(';') + 1);\n        var s = node['parsedSelector'] = node['selector'] = t.trim();\n        node['atRule'] = (s.indexOf(AT_START) === 0);\n        // note, support a subset of rule types...\n        if (node['atRule']) {\n            if (s.indexOf(MEDIA_START) === 0) {\n                node['type'] = types.MEDIA_RULE;\n            }\n            else if (s.match(RX.keyframesRule)) {\n                node['type'] = types.KEYFRAMES_RULE;\n                node['keyframesName'] = node['selector'].split(RX.multipleSpaces).pop();\n            }\n        }\n        else {\n            if (s.indexOf(VAR_START) === 0) {\n                node['type'] = types.MIXIN_RULE;\n            }\n            else {\n                node['type'] = types.STYLE_RULE;\n            }\n        }\n    }\n    var r$ = node['rules'];\n    if (r$) {\n        for (var i = 0, l = r$.length, r = void 0; (i < l) && (r = r$[i]); i++) {\n            parseCss(r, text);\n        }\n    }\n    return node;\n}\n/**\n * conversion of sort unicode escapes with spaces like `\\33 ` (and longer) into\n * expanded form that doesn't require trailing space `\\000033`\n * @param {string} s\n * @return {string}\n */\nfunction _expandUnicodeEscapes(s) {\n    return s.replace(/\\\\([0-9a-f]{1,6})\\s/gi, function () {\n        var code = arguments[1], repeat = 6 - code.length;\n        while (repeat--) {\n            code = '0' + code;\n        }\n        return '\\\\' + code;\n    });\n}\n/** @enum {number} */\nvar types = {\n    STYLE_RULE: 1,\n    KEYFRAMES_RULE: 7,\n    MEDIA_RULE: 4,\n    MIXIN_RULE: 1000\n};\nvar OPEN_BRACE = '{';\nvar CLOSE_BRACE = '}';\n// helper regexp's\nvar RX = {\n    comments: /\\/\\*[^*]*\\*+([^/*][^*]*\\*+)*\\//gim,\n    port: /@import[^;]*;/gim,\n    customProp: /(?:^[^;\\-\\s}]+)?--[^;{}]*?:[^{};]*?(?:[;\\n]|$)/gim,\n    mixinProp: /(?:^[^;\\-\\s}]+)?--[^;{}]*?:[^{};]*?{[^}]*?}(?:[;\\n]|$)?/gim,\n    mixinApply: /@apply\\s*\\(?[^);]*\\)?\\s*(?:[;\\n]|$)?/gim,\n    varApply: /[^;:]*?:[^;]*?var\\([^;]*\\)(?:[;\\n]|$)?/gim,\n    keyframesRule: /^@[^\\s]*keyframes/,\n    multipleSpaces: /\\s+/g\n};\nvar VAR_START = '--';\nvar MEDIA_START = '@media';\nvar AT_START = '@';\nfunction findRegex(regex, cssText, offset) {\n    regex['lastIndex'] = 0;\n    var r = cssText.substring(offset).match(regex);\n    if (r) {\n        var start = offset + r['index'];\n        return {\n            start: start,\n            end: start + r[0].length\n        };\n    }\n    return null;\n}\nvar VAR_USAGE_START = /\\bvar\\(/;\nvar VAR_ASSIGN_START = /\\B--[\\w-]+\\s*:/;\nvar COMMENTS = /\\/\\*[^*]*\\*+([^/*][^*]*\\*+)*\\//gim;\nvar TRAILING_LINES = /^[\\t ]+\\n/gm;\nfunction resolveVar(props, prop, fallback) {\n    if (props[prop]) {\n        return props[prop];\n    }\n    if (fallback) {\n        return executeTemplate(fallback, props);\n    }\n    return '';\n}\nfunction findVarEndIndex(cssText, offset) {\n    var count = 0;\n    var i = offset;\n    for (; i < cssText.length; i++) {\n        var c = cssText[i];\n        if (c === '(') {\n            count++;\n        }\n        else if (c === ')') {\n            count--;\n            if (count <= 0) {\n                return i + 1;\n            }\n        }\n    }\n    return i;\n}\nfunction parseVar(cssText, offset) {\n    var varPos = findRegex(VAR_USAGE_START, cssText, offset);\n    if (!varPos) {\n        return null;\n    }\n    var endVar = findVarEndIndex(cssText, varPos.start);\n    var varContent = cssText.substring(varPos.end, endVar - 1);\n    var _a = varContent.split(','), propName = _a[0], fallback = _a.slice(1);\n    return {\n        start: varPos.start,\n        end: endVar,\n        propName: propName.trim(),\n        fallback: fallback.length > 0 ? fallback.join(',').trim() : undefined\n    };\n}\nfunction compileVar(cssText, template, offset) {\n    var varMeta = parseVar(cssText, offset);\n    if (!varMeta) {\n        template.push(cssText.substring(offset, cssText.length));\n        return cssText.length;\n    }\n    var propName = varMeta.propName;\n    var fallback = varMeta.fallback != null ? compileTemplate(varMeta.fallback) : undefined;\n    template.push(cssText.substring(offset, varMeta.start), function (params) { return resolveVar(params, propName, fallback); });\n    return varMeta.end;\n}\nfunction executeTemplate(template, props) {\n    var final = '';\n    for (var i = 0; i < template.length; i++) {\n        var s = template[i];\n        final += (typeof s === 'string')\n            ? s\n            : s(props);\n    }\n    return final;\n}\nfunction findEndValue(cssText, offset) {\n    var onStr = false;\n    var double = false;\n    var i = offset;\n    for (; i < cssText.length; i++) {\n        var c = cssText[i];\n        if (onStr) {\n            if (double && c === '\"') {\n                onStr = false;\n            }\n            if (!double && c === '\\'') {\n                onStr = false;\n            }\n        }\n        else {\n            if (c === '\"') {\n                onStr = true;\n                double = true;\n            }\n            else if (c === '\\'') {\n                onStr = true;\n                double = false;\n            }\n            else if (c === ';') {\n                return i + 1;\n            }\n            else if (c === '}') {\n                return i;\n            }\n        }\n    }\n    return i;\n}\nfunction removeCustomAssigns(cssText) {\n    var final = '';\n    var offset = 0;\n    while (true) {\n        var assignPos = findRegex(VAR_ASSIGN_START, cssText, offset);\n        var start = assignPos ? assignPos.start : cssText.length;\n        final += cssText.substring(offset, start);\n        if (assignPos) {\n            offset = findEndValue(cssText, start);\n        }\n        else {\n            break;\n        }\n    }\n    return final;\n}\nfunction compileTemplate(cssText) {\n    var index = 0;\n    cssText = cssText.replace(COMMENTS, '');\n    cssText = removeCustomAssigns(cssText)\n        .replace(TRAILING_LINES, '');\n    var segments = [];\n    while (index < cssText.length) {\n        index = compileVar(cssText, segments, index);\n    }\n    return segments;\n}\nfunction resolveValues(selectors) {\n    var props = {};\n    selectors.forEach(function (selector) {\n        selector.declarations.forEach(function (dec) {\n            props[dec.prop] = dec.value;\n        });\n    });\n    var propsValues = {};\n    var entries = Object.entries(props);\n    var _loop_1 = function (i) {\n        var dirty = false;\n        entries.forEach(function (_a) {\n            var key = _a[0], value = _a[1];\n            var propValue = executeTemplate(value, propsValues);\n            if (propValue !== propsValues[key]) {\n                propsValues[key] = propValue;\n                dirty = true;\n            }\n        });\n        if (!dirty) {\n            return \"break\";\n        }\n    };\n    for (var i = 0; i < 10; i++) {\n        var state_1 = _loop_1();\n        if (state_1 === \"break\")\n            break;\n    }\n    return propsValues;\n}\nfunction getSelectors(root, index) {\n    if (index === void 0) {\n        index = 0;\n    }\n    if (!root.rules) {\n        return [];\n    }\n    var selectors = [];\n    root.rules\n        .filter(function (rule) { return rule.type === types.STYLE_RULE; })\n        .forEach(function (rule) {\n        var declarations = getDeclarations(rule.cssText);\n        if (declarations.length > 0) {\n            rule.parsedSelector.split(',').forEach(function (selector) {\n                selector = selector.trim();\n                selectors.push({\n                    selector: selector,\n                    declarations: declarations,\n                    specificity: computeSpecificity(),\n                    nu: index\n                });\n            });\n        }\n        index++;\n    });\n    return selectors;\n}\nfunction computeSpecificity(_selector) {\n    return 1;\n}\nvar IMPORTANT = '!important';\nvar FIND_DECLARATIONS = /(?:^|[;\\s{]\\s*)(--[\\w-]*?)\\s*:\\s*(?:((?:'(?:\\\\'|.)*?'|\"(?:\\\\\"|.)*?\"|\\([^)]*?\\)|[^};{])+)|\\{([^}]*)\\}(?:(?=[;\\s}])|$))/gm;\nfunction getDeclarations(cssText) {\n    var declarations = [];\n    var xArray;\n    while (xArray = FIND_DECLARATIONS.exec(cssText.trim())) {\n        var _a = normalizeValue(xArray[2]), value = _a.value, important = _a.important;\n        declarations.push({\n            prop: xArray[1].trim(),\n            value: compileTemplate(value),\n            important: important,\n        });\n    }\n    return declarations;\n}\nfunction normalizeValue(value) {\n    var regex = /\\s+/gim;\n    value = value.replace(regex, ' ').trim();\n    var important = value.endsWith(IMPORTANT);\n    if (important) {\n        value = value.substr(0, value.length - IMPORTANT.length).trim();\n    }\n    return {\n        value: value,\n        important: important\n    };\n}\nfunction getActiveSelectors(hostEl, hostScopeMap, globalScopes) {\n    // computes the css scopes that might affect this particular element\n    // avoiding using spread arrays to avoid ts helper fns when in es5\n    var scopes = [];\n    var scopesForElement = getScopesForElement(hostScopeMap, hostEl);\n    // globalScopes are always took into account\n    globalScopes.forEach(function (s) { return scopes.push(s); });\n    // the parent scopes are computed by walking parent dom until <html> is reached\n    scopesForElement.forEach(function (s) { return scopes.push(s); });\n    // each scope might have an array of associated selectors\n    // let's flatten the complete array of selectors from all the scopes\n    var selectorSet = getSelectorsForScopes(scopes);\n    // we filter to only the selectors that matches the hostEl\n    var activeSelectors = selectorSet.filter(function (selector) { return matches(hostEl, selector.selector); });\n    // sort selectors by specifity\n    return sortSelectors(activeSelectors);\n}\nfunction getScopesForElement(hostTemplateMap, node) {\n    var scopes = [];\n    while (node) {\n        var scope = hostTemplateMap.get(node);\n        if (scope) {\n            scopes.push(scope);\n        }\n        node = node.parentElement;\n    }\n    return scopes;\n}\nfunction getSelectorsForScopes(scopes) {\n    var selectors = [];\n    scopes.forEach(function (scope) {\n        selectors.push.apply(selectors, scope.selectors);\n    });\n    return selectors;\n}\nfunction sortSelectors(selectors) {\n    selectors.sort(function (a, b) {\n        if (a.specificity === b.specificity) {\n            return a.nu - b.nu;\n        }\n        return a.specificity - b.specificity;\n    });\n    return selectors;\n}\nfunction matches(el, selector) {\n    return selector === ':root' || selector === 'html' || el.matches(selector);\n}\nfunction parseCSS(original) {\n    var ast = parse(original);\n    var template = compileTemplate(original);\n    var selectors = getSelectors(ast);\n    return {\n        original: original,\n        template: template,\n        selectors: selectors,\n        usesCssVars: template.length > 1\n    };\n}\nfunction addGlobalStyle(globalScopes, styleEl) {\n    if (globalScopes.some(function (css) { return css.styleEl === styleEl; })) {\n        return false;\n    }\n    var css = parseCSS(styleEl.textContent);\n    css.styleEl = styleEl;\n    globalScopes.push(css);\n    return true;\n}\nfunction updateGlobalScopes(scopes) {\n    var selectors = getSelectorsForScopes(scopes);\n    var props = resolveValues(selectors);\n    scopes.forEach(function (scope) {\n        if (scope.usesCssVars) {\n            scope.styleEl.textContent = executeTemplate(scope.template, props);\n        }\n    });\n}\nfunction reScope(scope, scopeId) {\n    var template = scope.template.map(function (segment) {\n        return (typeof segment === 'string')\n            ? replaceScope(segment, scope.scopeId, scopeId)\n            : segment;\n    });\n    var selectors = scope.selectors.map(function (sel) {\n        return Object.assign(Object.assign({}, sel), { selector: replaceScope(sel.selector, scope.scopeId, scopeId) });\n    });\n    return Object.assign(Object.assign({}, scope), { template: template,\n        selectors: selectors,\n        scopeId: scopeId });\n}\nfunction replaceScope(original, oldScopeId, newScopeId) {\n    original = replaceAll(original, \"\\\\.\" + oldScopeId, \".\" + newScopeId);\n    return original;\n}\nfunction replaceAll(input, find, replace) {\n    return input.replace(new RegExp(find, 'g'), replace);\n}\nfunction loadDocument(doc, globalScopes) {\n    loadDocumentStyles(doc, globalScopes);\n    return loadDocumentLinks(doc, globalScopes).then(function () {\n        updateGlobalScopes(globalScopes);\n    });\n}\nfunction startWatcher(doc, globalScopes) {\n    var mutation = new MutationObserver(function () {\n        if (loadDocumentStyles(doc, globalScopes)) {\n            updateGlobalScopes(globalScopes);\n        }\n    });\n    mutation.observe(document.head, { childList: true });\n}\nfunction loadDocumentLinks(doc, globalScopes) {\n    var promises = [];\n    var linkElms = doc.querySelectorAll('link[rel=\"stylesheet\"][href]:not([data-no-shim])');\n    for (var i = 0; i < linkElms.length; i++) {\n        promises.push(addGlobalLink(doc, globalScopes, linkElms[i]));\n    }\n    return Promise.all(promises);\n}\nfunction loadDocumentStyles(doc, globalScopes) {\n    var styleElms = Array.from(doc.querySelectorAll('style:not([data-styles]):not([data-no-shim])'));\n    return styleElms\n        .map(function (style) { return addGlobalStyle(globalScopes, style); })\n        .some(Boolean);\n}\nfunction addGlobalLink(doc, globalScopes, linkElm) {\n    var url = linkElm.href;\n    return fetch(url).then(function (rsp) { return rsp.text(); }).then(function (text) {\n        if (hasCssVariables(text) && linkElm.parentNode) {\n            if (hasRelativeUrls(text)) {\n                text = fixRelativeUrls(text, url);\n            }\n            var styleEl = doc.createElement('style');\n            styleEl.setAttribute('data-styles', '');\n            styleEl.textContent = text;\n            addGlobalStyle(globalScopes, styleEl);\n            linkElm.parentNode.insertBefore(styleEl, linkElm);\n            linkElm.remove();\n        }\n    }).catch(function (err) {\n        console.error(err);\n    });\n}\n// This regexp tries to determine when a variable is declared, for example:\n//\n// .my-el { --highlight-color: green; }\n//\n// but we don't want to trigger when a classname uses \"--\" or a pseudo-class is\n// used. We assume that the only characters that can preceed a variable\n// declaration are \"{\", from an opening block, \";\" from a preceeding rule, or a\n// space. This prevents the regexp from matching a word in a selector, since\n// they would need to start with a \".\" or \"#\". (We assume element names don't\n// start with \"--\").\nvar CSS_VARIABLE_REGEXP = /[\\s;{]--[-a-zA-Z0-9]+\\s*:/m;\nfunction hasCssVariables(css) {\n    return css.indexOf('var(') > -1 || CSS_VARIABLE_REGEXP.test(css);\n}\n// This regexp find all url() usages with relative urls\nvar CSS_URL_REGEXP = /url[\\s]*\\([\\s]*['\"]?(?!(?:https?|data)\\:|\\/)([^\\'\\\"\\)]*)[\\s]*['\"]?\\)[\\s]*/gim;\nfunction hasRelativeUrls(css) {\n    CSS_URL_REGEXP.lastIndex = 0;\n    return CSS_URL_REGEXP.test(css);\n}\nfunction fixRelativeUrls(css, originalUrl) {\n    // get the basepath from the original import url\n    var basePath = originalUrl.replace(/[^/]*$/, '');\n    // replace the relative url, with the new relative url\n    return css.replace(CSS_URL_REGEXP, function (fullMatch, url) {\n        // rhe new relative path is the base path + uri\n        // TODO: normalize relative URL\n        var relativeUrl = basePath + url;\n        return fullMatch.replace(url, relativeUrl);\n    });\n}\nvar CustomStyle = /** @class */ (function () {\n    function CustomStyle(win, doc) {\n        this.win = win;\n        this.doc = doc;\n        this.count = 0;\n        this.hostStyleMap = new WeakMap();\n        this.hostScopeMap = new WeakMap();\n        this.globalScopes = [];\n        this.scopesMap = new Map();\n        this.didInit = false;\n    }\n    CustomStyle.prototype.initShim = function () {\n        var _this = this;\n        if (this.didInit) {\n            return Promise.resolve();\n        }\n        else {\n            this.didInit = true;\n            return new Promise(function (resolve) {\n                _this.win.requestAnimationFrame(function () {\n                    startWatcher(_this.doc, _this.globalScopes);\n                    loadDocument(_this.doc, _this.globalScopes).then(function () { return resolve(); });\n                });\n            });\n        }\n    };\n    CustomStyle.prototype.addLink = function (linkEl) {\n        var _this = this;\n        return addGlobalLink(this.doc, this.globalScopes, linkEl).then(function () {\n            _this.updateGlobal();\n        });\n    };\n    CustomStyle.prototype.addGlobalStyle = function (styleEl) {\n        if (addGlobalStyle(this.globalScopes, styleEl)) {\n            this.updateGlobal();\n        }\n    };\n    CustomStyle.prototype.createHostStyle = function (hostEl, cssScopeId, cssText, isScoped) {\n        if (this.hostScopeMap.has(hostEl)) {\n            throw new Error('host style already created');\n        }\n        var baseScope = this.registerHostTemplate(cssText, cssScopeId, isScoped);\n        var styleEl = this.doc.createElement('style');\n        styleEl.setAttribute('data-no-shim', '');\n        if (!baseScope.usesCssVars) {\n            // This component does not use (read) css variables\n            styleEl.textContent = cssText;\n        }\n        else if (isScoped) {\n            // This component is dynamic: uses css var and is scoped\n            styleEl['s-sc'] = cssScopeId = baseScope.scopeId + \"-\" + this.count;\n            styleEl.textContent = '/*needs update*/';\n            this.hostStyleMap.set(hostEl, styleEl);\n            this.hostScopeMap.set(hostEl, reScope(baseScope, cssScopeId));\n            this.count++;\n        }\n        else {\n            // This component uses css vars, but it's no-encapsulation (global static)\n            baseScope.styleEl = styleEl;\n            if (!baseScope.usesCssVars) {\n                styleEl.textContent = executeTemplate(baseScope.template, {});\n            }\n            this.globalScopes.push(baseScope);\n            this.updateGlobal();\n            this.hostScopeMap.set(hostEl, baseScope);\n        }\n        return styleEl;\n    };\n    CustomStyle.prototype.removeHost = function (hostEl) {\n        var css = this.hostStyleMap.get(hostEl);\n        if (css) {\n            css.remove();\n        }\n        this.hostStyleMap.delete(hostEl);\n        this.hostScopeMap.delete(hostEl);\n    };\n    CustomStyle.prototype.updateHost = function (hostEl) {\n        var scope = this.hostScopeMap.get(hostEl);\n        if (scope && scope.usesCssVars && scope.isScoped) {\n            var styleEl = this.hostStyleMap.get(hostEl);\n            if (styleEl) {\n                var selectors = getActiveSelectors(hostEl, this.hostScopeMap, this.globalScopes);\n                var props = resolveValues(selectors);\n                styleEl.textContent = executeTemplate(scope.template, props);\n            }\n        }\n    };\n    CustomStyle.prototype.updateGlobal = function () {\n        updateGlobalScopes(this.globalScopes);\n    };\n    CustomStyle.prototype.registerHostTemplate = function (cssText, scopeId, isScoped) {\n        var scope = this.scopesMap.get(scopeId);\n        if (!scope) {\n            scope = parseCSS(cssText);\n            scope.scopeId = scopeId;\n            scope.isScoped = isScoped;\n            this.scopesMap.set(scopeId, scope);\n        }\n        return scope;\n    };\n    return CustomStyle;\n}());\nvar win = window;\nfunction needsShim() {\n    return !(win.CSS && win.CSS.supports && win.CSS.supports('color', 'var(--c)'));\n}\nif (!win.__stencil_cssshim && needsShim()) {\n    win.__stencil_cssshim = new CustomStyle(win, document);\n}\n\n\n//# sourceURL=webpack:///../node_modules/@genexus/design-tokens-editor/dist/esm-es5/css-shim-6aaf713d-9b13816a.js?");

/***/ })

}]);