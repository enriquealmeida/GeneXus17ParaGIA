const e = "gemini";

let t, n, l, o = !1, s = !1, i = !1, r = null, c = 0, a = !1;

const f = "undefined" != typeof window ? window : {}, u = f.CSS, d = f.document || {
  head: {}
}, p = {
  _$$flags$$_: 0,
  _$$resourcesUrl$$_: "",
  jmp: e => e(),
  raf: e => requestAnimationFrame(e),
  ael: (e, t, n, l) => e.addEventListener(t, n, l),
  rel: (e, t, n, l) => e.removeEventListener(t, n, l),
  ce: (e, t) => new CustomEvent(e, t)
}, $ =  (() => (d.head.attachShadow + "").indexOf("[native") > -1)(), promiseResolve = e => Promise.resolve(e), m =  (() => {
  try {
    return new CSSStyleSheet, !0;
  } catch (e) {}
  return !1;
})(), addHostEventListeners = (e, t, n, l) => {
  n && n.map(([n, l, o]) => {
    const s = e, i = hostListenerProxy(t, o), r = hostListenerOpts(n);
    p.ael(s, l, i, r), (t._$$rmListeners$$_ = t._$$rmListeners$$_ || []).push(() => p.rel(s, l, i, r));
  });
}, hostListenerProxy = (e, t) => n => {
  256 /* isListenReady */ & e._$$flags$$_ ? 
  // instance is ready, let's call it's member method for this event
  e._$$lazyInstance$$_[t](n) : (e._$$queuedListeners$$_ = e._$$queuedListeners$$_ || []).push([ t, n ]);
}
// prettier-ignore
, hostListenerOpts = e => 0 != (2 /* Capture */ & e), b = new WeakMap, attachStyles = e => {
  const t = e._$$cmpMeta$$_, n = e._$$hostElement$$_, l = t._$$flags$$_, o = (t._$$tagName$$_, 
  () => {}), s = ((e, t, n, l) => {
    let o = getScopeId(t), s = D.get(o);
    if (
    // if an element is NOT connected then getRootNode() will return the wrong root node
    // so the fallback is to always use the document for the root node in those cases
    e = 11 /* DocumentFragment */ === e.nodeType ? e : d, s) if ("string" == typeof s) {
      e = e.head || e;
      let n, i = b.get(e);
      if (i || b.set(e, i = new Set), !i.has(o)) {
        if (p._$$cssShim$$_) {
          n = p._$$cssShim$$_.createHostStyle(l, o, s, !!(10 /* needsScopedEncapsulation */ & t._$$flags$$_));
          const e = n["s-sc"];
          e && (o = e, 
          // we don't want to add this styleID to the appliedStyles Set
          // since the cssVarShim might need to apply several different
          // stylesheets for the same component
          i = null);
        } else n = d.createElement("style"), n.innerHTML = s;
        e.insertBefore(n, e.querySelector("link")), i && i.add(o);
      }
    } else e.adoptedStyleSheets.includes(s) || (e.adoptedStyleSheets = [ ...e.adoptedStyleSheets, s ]);
    return o;
  })($ && n.shadowRoot ? n.shadowRoot : n.getRootNode(), t, e._$$modeName$$_, n);
  10 /* needsScopedEncapsulation */ & l && (
  // only required when we're NOT using native shadow dom (slot)
  // or this browser doesn't support native shadow dom
  // and this host element was NOT created with SSR
  // let's pick out the inner content for slot projection
  // create a node to represent where the original
  // content was first placed, which is useful later on
  // DOM WRITE!!
  n["s-sc"] = s, n.classList.add(s + "-h")), o();
}, getScopeId = (e, t) => "sc-" + e._$$tagName$$_
/**
 * Default style mode id
 */
/**
 * Reusable empty obj/array
 * Don't add values to these!!
 */ , h = {}, isComplexType = e => "object" === (
// https://jsperf.com/typeof-fn-object/5
e = typeof e) || "function" === e, y = "undefined" != typeof Deno, w = !(y || "undefined" == typeof global || "function" != typeof require || !global.process || "string" != typeof __filename || global.origin && "string" == typeof global.origin), g = (y && Deno.build.os, 
w ? process.cwd : y && Deno.cwd, w ? process.exit : y && Deno.exit, (e, t, ...n) => {
  let l = null, o = null, s = null, i = !1, r = !1, c = [];
  const walk = t => {
    for (let n = 0; n < t.length; n++) l = t[n], Array.isArray(l) ? walk(l) : null != l && "boolean" != typeof l && ((i = "function" != typeof e && !isComplexType(l)) && (l += ""), 
    i && r ? 
    // If the previous child was simple (string), we merge both
    c[c.length - 1]._$$text$$_ += l : 
    // Append a new vNode, if it's text, we create a text vNode
    c.push(i ? newVNode(null, l) : l), r = i);
  };
  if (walk(n), t) {
    // normalize class / classname attributes
    t.key && (o = t.key), t.name && (s = t.name);
    {
      const e = t.className || t.class;
      e && (t.class = "object" != typeof e ? e : Object.keys(e).filter(t => e[t]).join(" "));
    }
  }
  const a = newVNode(e, null);
  return a._$$attrs$$_ = t, c.length > 0 && (a._$$children$$_ = c), a._$$key$$_ = o, 
  a._$$name$$_ = s, a;
}), newVNode = (e, t) => {
  const n = {
    _$$flags$$_: 0,
    _$$tag$$_: e,
    _$$text$$_: t,
    _$$elm$$_: null,
    _$$children$$_: null,
    _$$attrs$$_: null,
    _$$key$$_: null,
    _$$name$$_: null
  };
  return n;
}, k = {}, setAccessor = (e, t, n, l, o, s) => {
  if (n !== l) {
    let r = isMemberInElement(e, t), c = t.toLowerCase();
    if ("class" === t) {
      const t = e.classList, o = parseClassList(n), s = parseClassList(l);
      t.remove(...o.filter(e => e && !s.includes(e))), t.add(...s.filter(e => e && !o.includes(e)));
    } else if ("style" === t) {
      for (const t in n) l && null != l[t] || (t.includes("-") ? e.style.removeProperty(t) : e.style[t] = "");
      for (const t in l) n && l[t] === n[t] || (t.includes("-") ? e.style.setProperty(t, l[t]) : e.style[t] = l[t]);
    } else if ("key" === t) ; else if ("ref" === t) 
    // minifier will clean this up
    l && l(e); else if (r || "o" !== t[0] || "n" !== t[1]) {
      // Set property if it exists and it's not a SVG
      const c = isComplexType(l);
      if ((r || c && null !== l) && !o) try {
        if (e.tagName.includes("-")) e[t] = l; else {
          let o = null == l ? "" : l;
          // Workaround for Safari, moving the <input> caret when re-assigning the same valued
                    "list" === t ? r = !1 : null != n && e[t] == o || (e[t] = o);
        }
      } catch (i) {}
      null == l || !1 === l ? !1 === l && "" !== e.getAttribute(t) || e.removeAttribute(t) : (!r || 4 /* isHost */ & s || o) && !c && (l = !0 === l ? "" : l, 
      e.setAttribute(t, l));
    } else 
    // Event Handlers
    // so if the member name starts with "on" and the 3rd characters is
    // a capital letter, and it's not already a member on the element,
    // then we're assuming it's an event listener
    // on- prefixed events
    // allows to be explicit about the dom event to listen without any magic
    // under the hood:
    // <my-cmp on-click> // listens for "click"
    // <my-cmp on-Click> // listens for "Click"
    // <my-cmp on-ionChange> // listens for "ionChange"
    // <my-cmp on-EVENTS> // listens for "EVENTS"
    t = "-" === t[2] ? t.slice(3) : isMemberInElement(f, c) ? c.slice(2) : c[2] + t.slice(3), 
    n && p.rel(e, t, n, !1), l && p.ael(e, t, l, !1);
  }
}, v = /\s/, parseClassList = e => e ? e.split(v) : [], updateElement = (e, t, n, l) => {
  // if the element passed in is a shadow root, which is a document fragment
  // then we want to be adding attrs/props to the shadow root's "host" element
  // if it's not a shadow root, then we add attrs/props to the same element
  const o = 11 /* DocumentFragment */ === t._$$elm$$_.nodeType && t._$$elm$$_.host ? t._$$elm$$_.host : t._$$elm$$_, s = e && e._$$attrs$$_ || h, i = t._$$attrs$$_ || h;
  // remove attributes no longer present on the vnode by setting them to undefined
  for (l in s) l in i || setAccessor(o, l, s[l], void 0, n, t._$$flags$$_);
  // add new & update changed attributes
  for (l in i) setAccessor(o, l, s[l], i[l], n, t._$$flags$$_);
}, createElm = (e, s, r, c) => {
  // tslint:disable-next-line: prefer-const
  let a, f, u, p = s._$$children$$_[r], $ = 0;
  if (o || (
  // remember for later we need to check to relocate nodes
  i = !0, "slot" === p._$$tag$$_ && (t && 
  // scoped css needs to add its scoped id to the parent element
  c.classList.add(t + "-s"), p._$$flags$$_ |= p._$$children$$_ ? // slot element has fallback content
  2 /* isSlotFallback */ : // slot element does not have fallback content
  1 /* isSlotReference */)), null !== p._$$text$$_) 
  // create text node
  a = p._$$elm$$_ = d.createTextNode(p._$$text$$_); else if (1 /* isSlotReference */ & p._$$flags$$_) 
  // create a slot reference node
  a = p._$$elm$$_ = slotReferenceDebugNode(p); else if (
  // create element
  a = p._$$elm$$_ = d.createElement(2 /* isSlotFallback */ & p._$$flags$$_ ? "slot-fb" : p._$$tag$$_), 
  updateElement(null, p, !1), (e => null != e)(t) && a["s-si"] !== t && 
  // if there is a scopeId and this is the initial render
  // then let's add the scopeId as a css class
  a.classList.add(a["s-si"] = t), p._$$children$$_) for ($ = 0; $ < p._$$children$$_.length; ++$) 
  // create the node
  f = createElm(e, p, $, a), 
  // return node could have been null
  f && 
  // append our new node
  a.appendChild(f);
  return a["s-hn"] = l, 3 /* isSlotReference */ & p._$$flags$$_ && (
  // remember the content reference comment
  a["s-sr"] = !0, 
  // remember the content reference comment
  a["s-cr"] = n, 
  // remember the slot name, or empty string for default slot
  a["s-sn"] = p._$$name$$_ || "", 
  // check if we've got an old vnode for this slot
  u = e && e._$$children$$_ && e._$$children$$_[r], u && u._$$tag$$_ === p._$$tag$$_ && e._$$elm$$_ && 
  // we've got an old slot vnode and the wrapper is being replaced
  // so let's move the old slot content back to it's original location
  putBackInOriginalLocation(e._$$elm$$_, !1)), a;
}, putBackInOriginalLocation = (e, t) => {
  p._$$flags$$_ |= 1 /* isTmpDisconnected */;
  const n = e.childNodes;
  for (let o = n.length - 1; o >= 0; o--) {
    const e = n[o];
    e["s-hn"] !== l && e["s-ol"] && (
    // // this child node in the old element is from another component
    // // remove this node from the old slot's parent
    // childNode.remove();
    // and relocate it back to it's original location
    parentReferenceNode(e).insertBefore(e, referenceNode(e)), 
    // remove the old original location comment entirely
    // later on the patch function will know what to do
    // and move this to the correct spot in need be
    e["s-ol"].remove(), e["s-ol"] = void 0, i = !0), t && putBackInOriginalLocation(e, t);
  }
  p._$$flags$$_ &= -2 /* isTmpDisconnected */;
}, addVnodes = (e, t, n, o, s, i) => {
  let r, c = e["s-cr"] && e["s-cr"].parentNode || e;
  for (c.shadowRoot && c.tagName === l && (c = c.shadowRoot); s <= i; ++s) o[s] && (r = createElm(null, n, s, e), 
  r && (o[s]._$$elm$$_ = r, c.insertBefore(r, referenceNode(t))));
}, removeVnodes = (e, t, n, l, o) => {
  for (;t <= n; ++t) (l = e[t]) && (o = l._$$elm$$_, callNodeRefs(l), 
  // we're removing this element
  // so it's possible we need to show slot fallback content now
  s = !0, o["s-ol"] ? 
  // remove the original location comment
  o["s-ol"].remove() : 
  // it's possible that child nodes of the node
  // that's being removed are slot nodes
  putBackInOriginalLocation(o, !0), 
  // remove the vnode's element from the dom
  o.remove());
}, isSameVnode = (e, t) => 
// compare if two vnode to see if they're "technically" the same
// need to have the same element tag, and same key to be the same
e._$$tag$$_ === t._$$tag$$_ && ("slot" === e._$$tag$$_ ? e._$$name$$_ === t._$$name$$_ : e._$$key$$_ === t._$$key$$_), referenceNode = e => e && e["s-ol"] || e, parentReferenceNode = e => (e["s-ol"] ? e["s-ol"] : e).parentNode, patch = (e, t) => {
  const n = t._$$elm$$_ = e._$$elm$$_, l = e._$$children$$_, o = t._$$children$$_, s = t._$$tag$$_, i = t._$$text$$_;
  let r;
  null === i ? ("slot" === s || 
  // either this is the first render of an element OR it's an update
  // AND we already know it's possible it could have changed
  // this updates the element's css classes, attrs, props, listeners, etc.
  updateElement(e, t, !1), null !== l && null !== o ? 
  // looks like there's child vnodes for both the old and new vnodes
  ((e, t, n, l) => {
    let o, s, i = 0, r = 0, c = 0, a = 0, f = t.length - 1, u = t[0], d = t[f], p = l.length - 1, $ = l[0], m = l[p];
    for (;i <= f && r <= p; ) if (null == u) 
    // Vnode might have been moved left
    u = t[++i]; else if (null == d) d = t[--f]; else if (null == $) $ = l[++r]; else if (null == m) m = l[--p]; else if (isSameVnode(u, $)) patch(u, $), 
    u = t[++i], $ = l[++r]; else if (isSameVnode(d, m)) patch(d, m), d = t[--f], m = l[--p]; else if (isSameVnode(u, m)) 
    // Vnode moved right
    "slot" !== u._$$tag$$_ && "slot" !== m._$$tag$$_ || putBackInOriginalLocation(u._$$elm$$_.parentNode, !1), 
    patch(u, m), e.insertBefore(u._$$elm$$_, d._$$elm$$_.nextSibling), u = t[++i], m = l[--p]; else if (isSameVnode(d, $)) 
    // Vnode moved left
    "slot" !== u._$$tag$$_ && "slot" !== m._$$tag$$_ || putBackInOriginalLocation(d._$$elm$$_.parentNode, !1), 
    patch(d, $), e.insertBefore(d._$$elm$$_, u._$$elm$$_), d = t[--f], $ = l[++r]; else {
      for (
      // createKeyToOldIdx
      c = -1, a = i; a <= f; ++a) if (t[a] && null !== t[a]._$$key$$_ && t[a]._$$key$$_ === $._$$key$$_) {
        c = a;
        break;
      }
      c >= 0 ? (s = t[c], s._$$tag$$_ !== $._$$tag$$_ ? o = createElm(t && t[r], n, c, e) : (patch(s, $), 
      t[c] = void 0, o = s._$$elm$$_), $ = l[++r]) : (
      // new element
      o = createElm(t && t[r], n, r, e), $ = l[++r]), o && parentReferenceNode(u._$$elm$$_).insertBefore(o, referenceNode(u._$$elm$$_));
    }
    i > f ? addVnodes(e, null == l[p + 1] ? null : l[p + 1]._$$elm$$_, n, l, r, p) : r > p && removeVnodes(t, i, f);
  })(n, l, t, o) : null !== o ? (
  // no old child vnodes, but there are new child vnodes to add
  null !== e._$$text$$_ && (
  // the old vnode was text, so be sure to clear it out
  n.textContent = ""), 
  // add the new vnode children
  addVnodes(n, null, t, o, 0, o.length - 1)) : null !== l && 
  // no new child vnodes, but there are old child vnodes to remove
  removeVnodes(l, 0, l.length - 1)) : (r = n["s-cr"]) ? 
  // this element has slotted content
  r.parentNode.textContent = i : e._$$text$$_ !== i && (
  // update the text content for the text only vnode
  // and also only if the text is different than before
  n.data = i);
}, updateFallbackSlotVisibility = e => {
  // tslint:disable-next-line: prefer-const
  let t, n, l, o, s, i, r = e.childNodes;
  for (n = 0, l = r.length; n < l; n++) if (t = r[n], 1 /* ElementNode */ === t.nodeType) {
    if (t["s-sr"]) for (
    // this is a slot fallback node
    // get the slot name for this slot reference node
    s = t["s-sn"], 
    // by default always show a fallback slot node
    // then hide it if there are other slots in the light dom
    t.hidden = !1, o = 0; o < l; o++) if (r[o]["s-hn"] !== t["s-hn"]) if (
    // this sibling node is from a different component
    i = r[o].nodeType, "" !== s) {
      // this is a named fallback slot node
      if (1 /* ElementNode */ === i && s === r[o].getAttribute("slot")) {
        t.hidden = !0;
        break;
      }
    } else 
    // this is a default fallback slot node
    // any element or text node (with content)
    // should hide the default fallback slot node
    if (1 /* ElementNode */ === i || 3 /* TextNode */ === i && "" !== r[o].textContent.trim()) {
      t.hidden = !0;
      break;
    }
    // keep drilling down
        updateFallbackSlotVisibility(t);
  }
}, _ = [], relocateSlotContent = e => {
  // tslint:disable-next-line: prefer-const
  let t, n, l, o, i, r, c = 0, a = e.childNodes, f = a.length;
  for (;c < f; c++) {
    if (t = a[c], t["s-sr"] && (n = t["s-cr"])) for (
    // first got the content reference comment node
    // then we got it's parent, which is where all the host content is in now
    l = n.parentNode.childNodes, o = t["s-sn"], r = l.length - 1; r >= 0; r--) n = l[r], 
    n["s-cn"] || n["s-nr"] || n["s-hn"] === t["s-hn"] || (
    // let's do some relocating to its new home
    // but never relocate a content reference node
    // that is suppose to always represent the original content location
    isNodeLocatedInSlot(n, o) ? (
    // it's possible we've already decided to relocate this node
    i = _.find(e => e._$$nodeToRelocate$$_ === n), 
    // made some changes to slots
    // let's make sure we also double check
    // fallbacks are correctly hidden or shown
    s = !0, n["s-sn"] = n["s-sn"] || o, i ? 
    // previously we never found a slot home for this node
    // but turns out we did, so let's remember it now
    i._$$slotRefNode$$_ = t : 
    // add to our list of nodes to relocate
    _.push({
      _$$slotRefNode$$_: t,
      _$$nodeToRelocate$$_: n
    }), n["s-sr"] && _.map(e => {
      isNodeLocatedInSlot(e._$$nodeToRelocate$$_, n["s-sn"]) && (i = _.find(e => e._$$nodeToRelocate$$_ === n), 
      i && !e._$$slotRefNode$$_ && (e._$$slotRefNode$$_ = i._$$slotRefNode$$_));
    })) : _.some(e => e._$$nodeToRelocate$$_ === n) || 
    // so far this element does not have a slot home, not setting slotRefNode on purpose
    // if we never find a home for this element then we'll need to hide it
    _.push({
      _$$nodeToRelocate$$_: n
    }));
    1 /* ElementNode */ === t.nodeType && relocateSlotContent(t);
  }
}, isNodeLocatedInSlot = (e, t) => 1 /* ElementNode */ === e.nodeType ? null === e.getAttribute("slot") && "" === t || e.getAttribute("slot") === t : e["s-sn"] === t || "" === t, callNodeRefs = e => {
  e._$$attrs$$_ && e._$$attrs$$_.ref && e._$$attrs$$_.ref(null), e._$$children$$_ && e._$$children$$_.map(callNodeRefs);
}, renderVdom = (e, r) => {
  const c = e._$$hostElement$$_, a = e._$$cmpMeta$$_, f = e._$$vnode$$_ || newVNode(null, null), u = (e => e && e._$$tag$$_ === k)
  /**
 * Production setAccessor() function based on Preact by
 * Jason Miller (@developit)
 * Licensed under the MIT License
 * https://github.com/developit/preact/blob/master/LICENSE
 *
 * Modified for Stencil's compiler and vdom
 */ (r) ? r : g(null, null, r);
  if (l = c.tagName, a._$$attrsToReflect$$_ && (u._$$attrs$$_ = u._$$attrs$$_ || {}, 
  a._$$attrsToReflect$$_.map(([e, t]) => u._$$attrs$$_[t] = c[e])), u._$$tag$$_ = null, 
  u._$$flags$$_ |= 4 /* isHost */ , e._$$vnode$$_ = u, u._$$elm$$_ = f._$$elm$$_ = c.shadowRoot || c, 
  t = c["s-sc"], n = c["s-cr"], o = $ && 0 != (1 /* shadowDomEncapsulation */ & a._$$flags$$_), 
  // always reset
  s = !1, 
  // synchronous patch
  patch(f, u), 
  // while we're moving nodes around existing nodes, temporarily disable
  // the disconnectCallback from working
  p._$$flags$$_ |= 1 /* isTmpDisconnected */ , i) {
    let e, t, n, l, o, s;
    relocateSlotContent(u._$$elm$$_);
    let i = 0;
    for (;i < _.length; i++) e = _[i], t = e._$$nodeToRelocate$$_, t["s-ol"] || (
    // add a reference node marking this node's original location
    // keep a reference to this node for later lookups
    n = originalLocationDebugNode(t), n["s-nr"] = t, t.parentNode.insertBefore(t["s-ol"] = n, t));
    for (i = 0; i < _.length; i++) if (e = _[i], t = e._$$nodeToRelocate$$_, e._$$slotRefNode$$_) {
      for (
      // by default we're just going to insert it directly
      // after the slot reference node
      l = e._$$slotRefNode$$_.parentNode, o = e._$$slotRefNode$$_.nextSibling, n = t["s-ol"]; n = n.previousSibling; ) if (s = n["s-nr"], 
      s && s["s-sn"] === t["s-sn"] && l === s.parentNode && (s = s.nextSibling, !s || !s["s-nr"])) {
        o = s;
        break;
      }
      (!o && l !== t.parentNode || t.nextSibling !== o) && t !== o && (!t["s-hn"] && t["s-ol"] && (
      // probably a component in the index.html that doesn't have it's hostname set
      t["s-hn"] = t["s-ol"].parentNode.nodeName), 
      // add it back to the dom but in its new home
      l.insertBefore(t, o));
    } else 
    // this node doesn't have a slot home to go to, so let's hide it
    1 /* ElementNode */ === t.nodeType && (t.hidden = !0);
  }
  s && updateFallbackSlotVisibility(u._$$elm$$_), 
  // done moving nodes around
  // allow the disconnect callback to work again
  p._$$flags$$_ &= -2 /* isTmpDisconnected */ , 
  // always reset
  _.length = 0;
}, slotReferenceDebugNode = e => d.createComment(`<slot${e._$$name$$_ ? ' name="' + e._$$name$$_ + '"' : ""}> (host=${l.toLowerCase()})`), originalLocationDebugNode = e => d.createComment("org-location for " + (e.localName ? `<${e.localName}> (host=${e["s-hn"]})` : `[${e.textContent}]`)), getElement = e => getHostRef(e)._$$hostElement$$_, createEvent = (e, t, n) => {
  const l = getElement(e);
  return {
    emit: e => emitEvent(l, t, {
      bubbles: !!(4 /* Bubbles */ & n),
      composed: !!(2 /* Composed */ & n),
      cancelable: !!(1 /* Cancellable */ & n),
      detail: e
    })
  };
}, emitEvent = (e, t, n) => {
  const l = p.ce(t, n);
  return e.dispatchEvent(l), l;
}, attachToAncestor = (e, t) => {
  t && !e._$$onRenderResolve$$_ && t["s-p"] && t["s-p"].push(new Promise(t => e._$$onRenderResolve$$_ = t));
}, scheduleUpdate = (e, t) => {
  if (e._$$flags$$_ |= 16 /* isQueuedForUpdate */ , 4 /* isWaitingForChildren */ & e._$$flags$$_) return void (e._$$flags$$_ |= 512 /* needsRerender */);
  attachToAncestor(e, e._$$ancestorComponent$$_);
  return L(() => dispatchHooks(e, t));
}, dispatchHooks = (e, t) => {
  const n = e._$$hostElement$$_, l = (e._$$cmpMeta$$_._$$tagName$$_, () => {}), o = e._$$lazyInstance$$_;
  let s;
  return t ? (e._$$flags$$_ |= 256 /* isListenReady */ , e._$$queuedListeners$$_ && (e._$$queuedListeners$$_.map(([e, t]) => safeCall(o, e, t)), 
  e._$$queuedListeners$$_ = null), emitLifecycleEvent(n, "componentWillLoad"), s = safeCall(o, "componentWillLoad")) : emitLifecycleEvent(n, "componentWillUpdate"), 
  emitLifecycleEvent(n, "componentWillRender"), l(), then(s, () => updateComponent(e, o, t));
}, updateComponent = (e, t, n) => {
  // updateComponent
  const l = e._$$hostElement$$_, o = (e._$$cmpMeta$$_._$$tagName$$_, () => {}), s = l["s-rc"];
  n && 
  // DOM WRITE!
  attachStyles(e);
  const i = (e._$$cmpMeta$$_._$$tagName$$_, () => {});
  // looks like we've got child nodes to render into this host element
  // or we need to update the css class/attrs on the host element
  // DOM WRITE!
  renderVdom(e, callRender(e, t)), p._$$cssShim$$_ && p._$$cssShim$$_.updateHost(l), 
  s && (
  // ok, so turns out there are some child host elements
  // waiting on this parent element to load
  // let's fire off all update callbacks waiting
  s.map(e => e()), l["s-rc"] = void 0), i(), o();
  {
    const t = l["s-p"], postUpdate = () => postUpdateComponent(e);
    0 === t.length ? postUpdate() : (Promise.all(t).then(postUpdate), e._$$flags$$_ |= 4 /* isWaitingForChildren */ , 
    t.length = 0);
  }
}, callRender = (e, t) => {
  try {
    r = t, t = t.render(), e._$$flags$$_ &= -17 /* isQueuedForUpdate */ , e._$$flags$$_ |= 2 /* hasRendered */;
  } catch (n) {
    consoleError(n);
  }
  return r = null, t;
}, getRenderingRef = () => r, postUpdateComponent = e => {
  e._$$cmpMeta$$_._$$tagName$$_;
  const t = e._$$hostElement$$_, endPostUpdate = () => {}, n = e._$$lazyInstance$$_, l = e._$$ancestorComponent$$_;
  emitLifecycleEvent(t, "componentDidRender"), 64 /* hasLoadedComponent */ & e._$$flags$$_ ? (emitLifecycleEvent(t, "componentDidUpdate"), 
  endPostUpdate()) : (e._$$flags$$_ |= 64 /* hasLoadedComponent */ , 
  // DOM WRITE!
  addHydratedFlag(t), safeCall(n, "componentDidLoad"), emitLifecycleEvent(t, "componentDidLoad"), 
  endPostUpdate(), e._$$onReadyResolve$$_(t), l || appDidLoad()), e._$$onInstanceResolve$$_(t), 
  e._$$onRenderResolve$$_ && (e._$$onRenderResolve$$_(), e._$$onRenderResolve$$_ = void 0), 
  512 /* needsRerender */ & e._$$flags$$_ && nextTick(() => scheduleUpdate(e, !1)), 
  e._$$flags$$_ &= -517 /* needsRerender */;
}
// ( •_•)
// ( •_•)>⌐■-■
// (⌐■_■)
, forceUpdate = e => {
  {
    const t = getHostRef(e), n = t._$$hostElement$$_.isConnected;
    // Returns "true" when the forced update was successfully scheduled
    return n && 2 /* hasRendered */ == (18 /* isQueuedForUpdate */ & t._$$flags$$_) && scheduleUpdate(t, !1), 
    n;
  }
}, appDidLoad = e => {
  addHydratedFlag(d.documentElement), p._$$flags$$_ |= 2 /* appLoaded */ , nextTick(() => emitEvent(f, "appload", {
    detail: {
      namespace: "gemini"
    }
  }));
}, safeCall = (e, t, n) => {
  if (e && e[t]) try {
    return e[t](n);
  } catch (l) {
    consoleError(l);
  }
}, then = (e, t) => e && e.then ? e.then(t) : t(), emitLifecycleEvent = (e, t) => {
  emitEvent(e, "stencil_" + t, {
    bubbles: !0,
    composed: !0,
    detail: {
      namespace: "gemini"
    }
  });
}, addHydratedFlag = e => e.classList.add("hydrated"), setValue = (e, t, n, l) => {
  // check our new property value against our internal value
  const o = getHostRef(e), s = o._$$instanceValues$$_.get(t), i = o._$$flags$$_, r = o._$$lazyInstance$$_;
  if (n = ((e, t) => 
  // ensure this value is of the correct prop type
  null == e || isComplexType(e) ? e : 4 /* Boolean */ & t ? "false" !== e && ("" === e || !!e) : 2 /* Number */ & t ? parseFloat(e) : 1 /* String */ & t ? e + "" : e)(n, l._$$members$$_[t][0]), 
  !(8 /* isConstructingInstance */ & i && void 0 !== s || n === s) && (
  // gadzooks! the property's value has changed!!
  // set our new value!
  o._$$instanceValues$$_.set(t, n), r)) {
    // get an array of method names of watch functions to call
    if (l._$$watchers$$_ && 128 /* isWatchReady */ & i) {
      const e = l._$$watchers$$_[t];
      e && 
      // this instance is watching for when this property changed
      e.map(e => {
        try {
          // fire off each of the watch methods that are watching this property
          r[e](n, s, t);
        } catch (l) {
          consoleError(l);
        }
      });
    }
    2 /* hasRendered */ == (18 /* isQueuedForUpdate */ & i) && 
    // looks like this value actually changed, so we've got work to do!
    // but only if we've already rendered, otherwise just chill out
    // queue that we need to do an update, but don't worry about queuing
    // up millions cuz this function ensures it only runs once
    scheduleUpdate(o, !1);
  }
}, proxyComponent = (e, t, n) => {
  if (t._$$members$$_) {
    e.watchers && (t._$$watchers$$_ = e.watchers);
    // It's better to have a const than two Object.entries()
        const l = Object.entries(t._$$members$$_), o = e.prototype;
    if (l.map(([e, [l]]) => {
      31 /* Prop */ & l || 2 /* proxyState */ & n && 32 /* State */ & l ? 
      // proxyComponent - prop
      Object.defineProperty(o, e, {
        get() {
          // proxyComponent, get value
          return ((e, t) => getHostRef(e)._$$instanceValues$$_.get(t))(this, e);
        },
        set(n) {
          // proxyComponent, set value
          setValue(this, e, n, t);
        },
        configurable: !0,
        enumerable: !0
      }) : 1 /* isElementConstructor */ & n && 64 /* Method */ & l && 
      // proxyComponent - method
      Object.defineProperty(o, e, {
        value(...t) {
          const n = getHostRef(this);
          return n._$$onInstancePromise$$_.then(() => n._$$lazyInstance$$_[e](...t));
        }
      });
    }), 1 /* isElementConstructor */ & n) {
      const n = new Map;
      o.attributeChangedCallback = function(e, t, l) {
        p.jmp(() => {
          const t = n.get(e);
          this[t] = (null !== l || "boolean" != typeof this[t]) && l;
        });
      }, 
      // create an array of attributes to observe
      // and also create a map of html attribute name to js property name
      e.observedAttributes = l.filter(([e, t]) => 15 /* HasAttribute */ & t[0] // filter to only keep props that should match attributes
      ).map(([e, l]) => {
        const o = l[1] || e;
        return n.set(o, e), 512 /* ReflectAttr */ & l[0] && t._$$attrsToReflect$$_.push([ e, o ]), 
        o;
      });
    }
  }
  return e;
}, initializeComponent = async (e, t, n, l, o) => {
  // initializeComponent
  if (0 == (32 /* hasInitializedComponent */ & t._$$flags$$_)) {
    {
      if (
      // we haven't initialized this element yet
      t._$$flags$$_ |= 32 /* hasInitializedComponent */ , (
      // lazy loaded components
      // request the component's implementation to be
      // wired up with the host element
      o = loadModule(n)).then) {
        // Await creates a micro-task avoid if possible
        const endLoad = () => {};
        o = await o, endLoad();
      }
      if (!o) throw Error(`Constructor for "${n._$$tagName$$_}#${t._$$modeName$$_}" was not found`);
      o.isProxied || (n._$$watchers$$_ = o.watchers, proxyComponent(o, n, 2 /* proxyState */), 
      o.isProxied = !0);
      const e = (n._$$tagName$$_, () => {});
      // ok, time to construct the instance
      // but let's keep track of when we start and stop
      // so that the getters/setters don't incorrectly step on data
            t._$$flags$$_ |= 8 /* isConstructingInstance */;
      // construct the lazy-loaded component implementation
      // passing the hostRef is very important during
      // construction in order to directly wire together the
      // host element and the lazy-loaded instance
      try {
        new o(t);
      } catch (i) {
        consoleError(i);
      }
      t._$$flags$$_ &= -9 /* isConstructingInstance */ , t._$$flags$$_ |= 128 /* isWatchReady */ , 
      e(), fireConnectedCallback(t._$$lazyInstance$$_);
    }
    if (o.style) {
      // this component has styles but we haven't registered them yet
      let e = o.style;
      const t = getScopeId(n);
      if (!D.has(t)) {
        const l = (n._$$tagName$$_, () => {});
        8 /* needsShadowDomShim */ & n._$$flags$$_ && (e = await __sc_import_gemini("./p-7faff4bb.js").then(n => n.scopeCss(e, t, !1))), 
        ((e, t, n) => {
          let l = D.get(e);
          m && n ? (l = l || new CSSStyleSheet, l.replace(t)) : l = t, D.set(e, l);
        })(t, e, !!(1 /* shadowDomEncapsulation */ & n._$$flags$$_)), l();
      }
    }
  }
  // we've successfully created a lazy instance
    const s = t._$$ancestorComponent$$_, schedule = () => scheduleUpdate(t, !0);
  s && s["s-rc"] ? 
  // this is the intial load and this component it has an ancestor component
  // but the ancestor component has NOT fired its will update lifecycle yet
  // so let's just cool our jets and wait for the ancestor to continue first
  // this will get fired off when the ancestor component
  // finally gets around to rendering its lazy self
  // fire off the initial update
  s["s-rc"].push(schedule) : schedule();
}, fireConnectedCallback = e => {
  safeCall(e, "connectedCallback");
}, setContentReference = e => {
  // only required when we're NOT using native shadow dom (slot)
  // or this browser doesn't support native shadow dom
  // and this host element was NOT created with SSR
  // let's pick out the inner content for slot projection
  // create a node to represent where the original
  // content was first placed, which is useful later on
  const t = e["s-cr"] = d.createComment(`content-ref (host=${e.localName})`);
  t["s-cn"] = !0, e.insertBefore(t, e.firstChild);
}, bootstrapLazy = (e, t = {}) => {
  const endBootstrap = () => {}, n = [], l = t.exclude || [], o = f.customElements, s = d.head, i =  s.querySelector("meta[charset]"), r =  d.createElement("style"), c = [];
  let a, u = !0;
  Object.assign(p, t), p._$$resourcesUrl$$_ = new URL(t.resourcesUrl || "./", d.baseURI).href, 
  t.syncQueue && (p._$$flags$$_ |= 4 /* queueSync */), e.map(e => e[1].map(t => {
    const s = {
      _$$flags$$_: t[0],
      _$$tagName$$_: t[1],
      _$$members$$_: t[2],
      _$$listeners$$_: t[3]
    };
    s._$$members$$_ = t[2], s._$$listeners$$_ = t[3], s._$$attrsToReflect$$_ = [], s._$$watchers$$_ = {}, 
    !$ && 1 /* shadowDomEncapsulation */ & s._$$flags$$_ && (s._$$flags$$_ |= 8 /* needsShadowDomShim */);
    const i = s._$$tagName$$_, r = class extends HTMLElement {
      // StencilLazyHost
      constructor(e) {
        // @ts-ignore
        super(e), registerHost(e = this, s), 1 /* shadowDomEncapsulation */ & s._$$flags$$_ && (
        // this component is using shadow dom
        // and this browser supports shadow dom
        // add the read-only property "shadowRoot" to the host element
        // adding the shadow root build conditionals to minimize runtime
        $ ? e.attachShadow({
          mode: "open"
        }) : "shadowRoot" in e || (e.shadowRoot = e));
      }
      connectedCallback() {
        a && (clearTimeout(a), a = null), u ? 
        // connectedCallback will be processed once all components have been registered
        c.push(this) : p.jmp(() => (e => {
          if (0 == (1 /* isTmpDisconnected */ & p._$$flags$$_)) {
            const t = getHostRef(e), n = t._$$cmpMeta$$_, l = (n._$$tagName$$_, () => {});
            if (1 /* hasConnected */ & t._$$flags$$_) 
            // not the first time this has connected
            // reattach any event listeners to the host
            // since they would have been removed when disconnected
            addHostEventListeners(e, t, n._$$listeners$$_), 
            // fire off connectedCallback() on component instance
            fireConnectedCallback(t._$$lazyInstance$$_); else {
              // first time this component has connected
              t._$$flags$$_ |= 1 /* hasConnected */ , 
              // initUpdate
              // if the slot polyfill is required we'll need to put some nodes
              // in here to act as original content anchors as we move nodes around
              // host element has been connected to the DOM
              12 /* needsShadowDomShim */ & n._$$flags$$_ && setContentReference(e);
              {
                // find the first ancestor component (if there is one) and register
                // this component as one of the actively loading child components for its ancestor
                let n = e;
                for (;n = n.parentNode || n.host; ) 
                // climb up the ancestors looking for the first
                // component that hasn't finished its lifecycle update yet
                if (n["s-p"]) {
                  // we found this components first ancestor component
                  // keep a reference to this component's ancestor component
                  attachToAncestor(t, t._$$ancestorComponent$$_ = n);
                  break;
                }
              }
              // Lazy properties
              // https://developers.google.com/web/fundamentals/web-components/best-practices#lazy-properties
                            n._$$members$$_ && Object.entries(n._$$members$$_).map(([t, [n]]) => {
                if (31 /* Prop */ & n && e.hasOwnProperty(t)) {
                  const n = e[t];
                  delete e[t], e[t] = n;
                }
              }), 
              // connectedCallback, taskQueue, initialLoad
              // angular sets attribute AFTER connectCallback
              // https://github.com/angular/angular/issues/18909
              // https://github.com/angular/angular/issues/19940
              nextTick(() => initializeComponent(0, t, n));
            }
            l();
          }
        })(this));
      }
      disconnectedCallback() {
        p.jmp(() => (e => {
          if (0 == (1 /* isTmpDisconnected */ & p._$$flags$$_)) {
            const t = getHostRef(e), n = t._$$lazyInstance$$_;
            t._$$rmListeners$$_ && (t._$$rmListeners$$_.map(e => e()), t._$$rmListeners$$_ = void 0), 
            // clear CSS var-shim tracking
            p._$$cssShim$$_ && p._$$cssShim$$_.removeHost(e), safeCall(n, "disconnectedCallback"), 
            safeCall(n, "componentDidUnload");
          }
        })(this));
      }
      forceUpdate() {
        forceUpdate(this);
      }
      componentOnReady() {
        return getHostRef(this)._$$onReadyPromise$$_;
      }
    };
    s._$$lazyBundleId$$_ = e[0], l.includes(i) || o.get(i) || (n.push(i), o.define(i, proxyComponent(r, s, 1 /* isElementConstructor */)));
  })), r.innerHTML = n + "{visibility:hidden}.hydrated{visibility:inherit}", r.setAttribute("data-styles", ""), 
  s.insertBefore(r, i ? i.nextSibling : s.firstChild), 
  // Process deferred connectedCallbacks now all components have been registered
  u = !1, c.length ? c.map(e => e.connectedCallback()) : p.jmp(() => a = setTimeout(appDidLoad, 30)), 
  // Fallback appLoad event
  endBootstrap();
}, getAssetPath = e => {
  const t = new URL(e, p._$$resourcesUrl$$_);
  return t.origin !== f.location.origin ? t.href : t.pathname;
}, j = new WeakMap, getHostRef = e => j.get(e), registerInstance = (e, t) => j.set(t._$$lazyInstance$$_ = e, t), registerHost = (e, t) => {
  const n = {
    _$$flags$$_: 0,
    _$$hostElement$$_: e,
    _$$cmpMeta$$_: t,
    _$$instanceValues$$_: new Map
  };
  return n._$$onInstancePromise$$_ = new Promise(e => n._$$onInstanceResolve$$_ = e), 
  n._$$onReadyPromise$$_ = new Promise(e => n._$$onReadyResolve$$_ = e), e["s-p"] = [], 
  e["s-rc"] = [], addHostEventListeners(e, n, t._$$listeners$$_), j.set(e, n);
}, isMemberInElement = (e, t) => t in e, consoleError = e => console.error(e), C =  new Map, loadModule = (e, t, n) => {
  // loadModuleImport
  const l = e._$$tagName$$_.replace(/-/g, "_"), o = e._$$lazyBundleId$$_, s = C.get(o);
  return s ? s[l] : __sc_import_gemini(
  /* webpackInclude: /\.entry\.js$/ */
  /* webpackExclude: /\.system\.entry\.js$/ */
  /* webpackMode: "lazy" */
  `./${o}.entry.js`).then(e => (C.set(o, e), e[l]), consoleError);
}, D = new Map, M = [], R = [], S = [], queueTask = (e, t) => n => {
  e.push(n), a || (a = !0, t && 4 /* queueSync */ & p._$$flags$$_ ? nextTick(flush) : p.raf(flush));
}, consumeTimeout = (e, t) => {
  let n = 0, l = 0;
  for (;n < e.length && (l = performance.now()) < t; ) try {
    e[n++](l);
  } catch (o) {
    consoleError(o);
  }
  n === e.length ? e.length = 0 : 0 !== n && e.splice(0, n);
}, flush = () => {
  c++, 
  // always force a bunch of medium callbacks to run, but still have
  // a throttle on how many can run in a certain time
  // DOM READS!!!
  (e => {
    for (let n = 0; n < e.length; n++) try {
      e[n](performance.now());
    } catch (t) {
      consoleError(t);
    }
    e.length = 0;
  })(M);
  // DOM WRITES!!!
  {
    const e = 2 /* appLoaded */ == (6 /* queueMask */ & p._$$flags$$_) ? performance.now() + 14 * Math.ceil(.1 * c) : 1 / 0;
    consumeTimeout(R, e), consumeTimeout(S, e), R.length > 0 && (S.push(...R), R.length = 0), 
    (a = M.length + R.length + S.length > 0) ? 
    // still more to do yet, but we've run out of time
    // let's let this thing cool off and try again in the next tick
    p.raf(flush) : c = 0;
  }
}, nextTick =  e => promiseResolve().then(e), L =  queueTask(R, !0);

export { u as C, k as H, e as N, promiseResolve as a, bootstrapLazy as b, createEvent as c, d, getAssetPath as e, getRenderingRef as f, getElement as g, g as h, forceUpdate as i, p, registerInstance as r, f as w }