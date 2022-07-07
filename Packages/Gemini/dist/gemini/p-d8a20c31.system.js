var __extends = this && this.__extends || function() {
  var extendStatics = function(e, n) {
    return (extendStatics = Object.setPrototypeOf || {
      __proto__: []
    } instanceof Array && function(e, n) {
      e.__proto__ = n;
    } || function(e, n) {
      for (var t in n) n.hasOwnProperty(t) && (e[t] = n[t]);
    })(e, n);
  };
  return function(e, n) {
    function __() {
      this.constructor = e;
    }
    extendStatics(e, n), e.prototype = null === n ? Object.create(n) : (__.prototype = n.prototype, 
    new __);
  };
}(), __awaiter = this && this.__awaiter || function(e, n, t, r) {
  return new (t || (t = Promise))((function(o, s) {
    function fulfilled(e) {
      try {
        step(r.next(e));
      } catch (n) {
        s(n);
      }
    }
    function rejected(e) {
      try {
        step(r.throw(e));
      } catch (n) {
        s(n);
      }
    }
    function step(e) {
      var n;
      e.done ? o(e.value) : (n = e.value, n instanceof t ? n : new t((function(e) {
        e(n);
      }))).then(fulfilled, rejected);
    }
    step((r = r.apply(e, n || [])).next());
  }));
}, __generator = this && this.__generator || function(e, n) {
  var t, r, o, s, l = {
    label: 0,
    sent: function() {
      if (1 & o[0]) throw o[1];
      return o[1];
    },
    trys: [],
    ops: []
  };
  return s = {
    next: verb(0),
    throw: verb(1),
    return: verb(2)
  }, "function" == typeof Symbol && (s[Symbol.iterator] = function() {
    return this;
  }), s;
  function verb(s) {
    return function(a) {
      return function(s) {
        if (t) throw new TypeError("Generator is already executing.");
        for (;l; ) try {
          if (t = 1, r && (o = 2 & s[0] ? r.return : s[0] ? r.throw || ((o = r.return) && o.call(r), 
          0) : r.next) && !(o = o.call(r, s[1])).done) return o;
          switch (r = 0, o && (s = [ 2 & s[0], o.value ]), s[0]) {
           case 0:
           case 1:
            o = s;
            break;

           case 4:
            return l.label++, {
              value: s[1],
              done: !1
            };

           case 5:
            l.label++, r = s[1], s = [ 0 ];
            continue;

           case 7:
            s = l.ops.pop(), l.trys.pop();
            continue;

           default:
            if (!(o = l.trys, (o = o.length > 0 && o[o.length - 1]) || 6 !== s[0] && 2 !== s[0])) {
              l = 0;
              continue;
            }
            if (3 === s[0] && (!o || s[1] > o[0] && s[1] < o[3])) {
              l.label = s[1];
              break;
            }
            if (6 === s[0] && l.label < o[1]) {
              l.label = o[1], o = s;
              break;
            }
            if (o && l.label < o[2]) {
              l.label = o[2], l.ops.push(s);
              break;
            }
            o[2] && l.ops.pop(), l.trys.pop();
            continue;
          }
          s = n.call(e, l);
        } catch (a) {
          s = [ 6, a ], r = 0;
        } finally {
          t = o = 0;
        }
        if (5 & s[0]) throw s[1];
        return {
          value: s[0] ? s[1] : void 0,
          done: !0
        };
      }([ s, a ]);
    };
  }
}, __spreadArrays = this && this.__spreadArrays || function() {
  for (var e = 0, n = 0, t = arguments.length; n < t; n++) e += arguments[n].length;
  var r = Array(e), o = 0;
  for (n = 0; n < t; n++) for (var s = arguments[n], l = 0, a = s.length; l < a; l++, 
  o++) r[o] = s[l];
  return r;
};

System.register([], (function(e, n) {
  "use strict";
  return {
    execute: function() {
      var t, r, o, s = this, l = e("N", "gemini"), a = !1, $ = !1, i = !1, c = null, u = 0, f = !1, d = e("w", "undefined" != typeof window ? window : {}), m = (e("C", d.CSS), 
      e("d", d.document || {
        head: {}
      })), h = e("p", {
        $flags$: 0,
        $resourcesUrl$: "",
        jmp: function(e) {
          return e();
        },
        raf: function(e) {
          return requestAnimationFrame(e);
        },
        ael: function(e, n, t, r) {
          return e.addEventListener(n, t, r);
        },
        rel: function(e, n, t, r) {
          return e.removeEventListener(n, t, r);
        },
        ce: function(e, n) {
          return new CustomEvent(e, n);
        }
      }), p =  function() {
        return (m.head.attachShadow + "").indexOf("[native") > -1;
      }(), g = e("a", (function(e) {
        return Promise.resolve(e);
      })), v =  function() {
        try {
          return new CSSStyleSheet, !0;
        } catch (e) {}
        return !1;
      }(), addHostEventListeners = function(e, n, t, r) {
        t && t.map((function(t) {
          var r = t[0], o = t[1], s = t[2], l = e, a = hostListenerProxy(n, s), $ = hostListenerOpts(r);
          h.ael(l, o, a, $), (n.$rmListeners$ = n.$rmListeners$ || []).push((function() {
            return h.rel(l, o, a, $);
          }));
        }));
      }, hostListenerProxy = function(e, n) {
        return function(t) {
          256 /* isListenReady */ & e.$flags$ ? 
          // instance is ready, let's call it's member method for this event
          e.$lazyInstance$[n](t) : (e.$queuedListeners$ = e.$queuedListeners$ || []).push([ n, t ]);
        };
      }, hostListenerOpts = function(e) {
        return 0 != (2 /* Capture */ & e);
      }, createTime = function(e, n) {
        return void 0 === n && (n = ""), function() {};
      }, y = new WeakMap, attachStyles = function(e) {
        var n = e.$cmpMeta$, t = e.$hostElement$, r = n.$flags$, o = createTime(0, n.$tagName$), s = function(e, n, t, r) {
          var o = getScopeId(n), s = M.get(o);
          if (
          // if an element is NOT connected then getRootNode() will return the wrong root node
          // so the fallback is to always use the document for the root node in those cases
          e = 11 /* DocumentFragment */ === e.nodeType ? e : m, s) if ("string" == typeof s) {
            e = e.head || e;
            var l = y.get(e), a = void 0;
            if (l || y.set(e, l = new Set), !l.has(o)) {
              if (h.$cssShim$) {
                var $ = (a = h.$cssShim$.createHostStyle(r, o, s, !!(10 /* needsScopedEncapsulation */ & n.$flags$)))["s-sc"];
                $ && (o = $, 
                // we don't want to add this styleID to the appliedStyles Set
                // since the cssVarShim might need to apply several different
                // stylesheets for the same component
                l = null);
              } else (a = m.createElement("style")).innerHTML = s;
              e.insertBefore(a, e.querySelector("link")), l && l.add(o);
            }
          } else e.adoptedStyleSheets.includes(s) || (e.adoptedStyleSheets = __spreadArrays(e.adoptedStyleSheets, [ s ]));
          return o;
        }(p && t.shadowRoot ? t.shadowRoot : t.getRootNode(), n, e.$modeName$, t);
        10 /* needsScopedEncapsulation */ & r && (
        // only required when we're NOT using native shadow dom (slot)
        // or this browser doesn't support native shadow dom
        // and this host element was NOT created with SSR
        // let's pick out the inner content for slot projection
        // create a node to represent where the original
        // content was first placed, which is useful later on
        // DOM WRITE!!
        t["s-sc"] = s, t.classList.add(s + "-h")), o();
      }, getScopeId = function(e, n) {
        return "sc-" + e.$tagName$;
      }, b = {}, isComplexType = function(e) {
        return "object" === (
        // https://jsperf.com/typeof-fn-object/5
        e = typeof e) || "function" === e;
      }, w = "undefined" != typeof Deno, R = !(w || "undefined" == typeof global || "function" != typeof require || !global.process || "string" != typeof __filename || global.origin && "string" == typeof global.origin), N = (w && Deno.build.os, 
      R ? process.cwd : w && Deno.cwd, R ? process.exit : w && Deno.exit, e("h", (function(e, n) {
        for (var t = [], r = 2; r < arguments.length; r++) t[r - 2] = arguments[r];
        var o = null, s = null, l = null, a = !1, $ = !1, i = [], walk = function(n) {
          for (var t = 0; t < n.length; t++) o = n[t], Array.isArray(o) ? walk(o) : null != o && "boolean" != typeof o && ((a = "function" != typeof e && !isComplexType(o)) && (o = String(o)), 
          a && $ ? 
          // If the previous child was simple (string), we merge both
          i[i.length - 1].$text$ += o : 
          // Append a new vNode, if it's text, we create a text vNode
          i.push(a ? newVNode(null, o) : o), $ = a);
        };
        if (walk(t), n) {
          // normalize class / classname attributes
          n.key && (s = n.key), n.name && (l = n.name);
          var c = n.className || n.class;
          c && (n.class = "object" != typeof c ? c : Object.keys(c).filter((function(e) {
            return c[e];
          })).join(" "));
        }
        var u = newVNode(e, null);
        return u.$attrs$ = n, i.length > 0 && (u.$children$ = i), u.$key$ = s, u.$name$ = l, 
        u;
      }))), newVNode = function(e, n) {
        var t = {
          $flags$: 0,
          $tag$: e,
          $text$: n,
          $elm$: null,
          $children$: null,
          $attrs$: null,
          $key$: null,
          $name$: null
        };
        return t;
      }, _ = e("H", {}), setAccessor = function(e, n, t, r, o, s) {
        if (t !== r) {
          var l = isMemberInElement(e, n), a = n.toLowerCase();
          if ("class" === n) {
            var $ = e.classList, i = parseClassList(t), c = parseClassList(r);
            $.remove.apply($, i.filter((function(e) {
              return e && !c.includes(e);
            }))), $.add.apply($, c.filter((function(e) {
              return e && !i.includes(e);
            })));
          } else if ("style" === n) {
            for (var u in t) r && null != r[u] || (u.includes("-") ? e.style.removeProperty(u) : e.style[u] = "");
            for (var u in r) t && r[u] === t[u] || (u.includes("-") ? e.style.setProperty(u, r[u]) : e.style[u] = r[u]);
          } else if ("key" === n) ; else if ("ref" === n) 
          // minifier will clean this up
          r && r(e); else if (l || "o" !== n[0] || "n" !== n[1]) {
            // Set property if it exists and it's not a SVG
            var f = isComplexType(r);
            if ((l || f && null !== r) && !o) try {
              if (e.tagName.includes("-")) e[n] = r; else {
                var m = null == r ? "" : r;
                // Workaround for Safari, moving the <input> caret when re-assigning the same valued
                                "list" === n ? l = !1 : null != t && e[n] == m || (e[n] = m);
              }
            } catch (p) {}
            null == r || !1 === r ? !1 === r && "" !== e.getAttribute(n) || e.removeAttribute(n) : (!l || 4 /* isHost */ & s || o) && !f && (r = !0 === r ? "" : r, 
            e.setAttribute(n, r));
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
          n = "-" === n[2] ? n.slice(3) : isMemberInElement(d, a) ? a.slice(2) : a[2] + n.slice(3), 
          t && h.rel(e, n, t, !1), r && h.ael(e, n, r, !1);
        }
      }, S = /\s/, parseClassList = function(e) {
        return e ? e.split(S) : [];
      }, updateElement = function(e, n, t, r) {
        // if the element passed in is a shadow root, which is a document fragment
        // then we want to be adding attrs/props to the shadow root's "host" element
        // if it's not a shadow root, then we add attrs/props to the same element
        var o = 11 /* DocumentFragment */ === n.$elm$.nodeType && n.$elm$.host ? n.$elm$.host : n.$elm$, s = e && e.$attrs$ || b, l = n.$attrs$ || b;
        // remove attributes no longer present on the vnode by setting them to undefined
        for (r in s) r in l || setAccessor(o, r, s[r], void 0, t, n.$flags$);
        // add new & update changed attributes
        for (r in l) setAccessor(o, r, s[r], l[r], t, n.$flags$);
      }, createElm = function(e, n, s, l) {
        // tslint:disable-next-line: prefer-const
        var $, c, u, f = n.$children$[s], d = 0;
        if (a || (
        // remember for later we need to check to relocate nodes
        i = !0, "slot" === f.$tag$ && (t && 
        // scoped css needs to add its scoped id to the parent element
        l.classList.add(t + "-s"), f.$flags$ |= f.$children$ ? // slot element has fallback content
        2 /* isSlotFallback */ : // slot element does not have fallback content
        1 /* isSlotReference */)), null !== f.$text$) 
        // create text node
        $ = f.$elm$ = m.createTextNode(f.$text$); else if (1 /* isSlotReference */ & f.$flags$) 
        // create a slot reference node
        $ = f.$elm$ = slotReferenceDebugNode(f); else if (
        // create element
        $ = f.$elm$ = m.createElement(2 /* isSlotFallback */ & f.$flags$ ? "slot-fb" : f.$tag$), 
        updateElement(null, f, !1), null != t && $["s-si"] !== t && 
        // if there is a scopeId and this is the initial render
        // then let's add the scopeId as a css class
        $.classList.add($["s-si"] = t), f.$children$) for (d = 0; d < f.$children$.length; ++d) 
        // create the node
        // return node could have been null
        (c = createElm(e, f, d, $)) && 
        // append our new node
        $.appendChild(c);
        return $["s-hn"] = o, 3 /* isSlotReference */ & f.$flags$ && (
        // remember the content reference comment
        $["s-sr"] = !0, 
        // remember the content reference comment
        $["s-cr"] = r, 
        // remember the slot name, or empty string for default slot
        $["s-sn"] = f.$name$ || "", (
        // check if we've got an old vnode for this slot
        u = e && e.$children$ && e.$children$[s]) && u.$tag$ === f.$tag$ && e.$elm$ && 
        // we've got an old slot vnode and the wrapper is being replaced
        // so let's move the old slot content back to it's original location
        putBackInOriginalLocation(e.$elm$, !1)), $;
      }, putBackInOriginalLocation = function(e, n) {
        h.$flags$ |= 1 /* isTmpDisconnected */;
        for (var t = e.childNodes, r = t.length - 1; r >= 0; r--) {
          var s = t[r];
          s["s-hn"] !== o && s["s-ol"] && (
          // // this child node in the old element is from another component
          // // remove this node from the old slot's parent
          // childNode.remove();
          // and relocate it back to it's original location
          parentReferenceNode(s).insertBefore(s, referenceNode(s)), 
          // remove the old original location comment entirely
          // later on the patch function will know what to do
          // and move this to the correct spot in need be
          s["s-ol"].remove(), s["s-ol"] = void 0, i = !0), n && putBackInOriginalLocation(s, n);
        }
        h.$flags$ &= -2 /* isTmpDisconnected */;
      }, addVnodes = function(e, n, t, r, s, l) {
        var a, $ = e["s-cr"] && e["s-cr"].parentNode || e;
        for ($.shadowRoot && $.tagName === o && ($ = $.shadowRoot); s <= l; ++s) r[s] && (a = createElm(null, t, s, e)) && (r[s].$elm$ = a, 
        $.insertBefore(a, referenceNode(n)));
      }, removeVnodes = function(e, n, t, r, o) {
        for (;n <= t; ++n) (r = e[n]) && (o = r.$elm$, callNodeRefs(r), 
        // we're removing this element
        // so it's possible we need to show slot fallback content now
        $ = !0, o["s-ol"] ? 
        // remove the original location comment
        o["s-ol"].remove() : 
        // it's possible that child nodes of the node
        // that's being removed are slot nodes
        putBackInOriginalLocation(o, !0), 
        // remove the vnode's element from the dom
        o.remove());
      }, isSameVnode = function(e, n) {
        // compare if two vnode to see if they're "technically" the same
        // need to have the same element tag, and same key to be the same
        return e.$tag$ === n.$tag$ && ("slot" === e.$tag$ ? e.$name$ === n.$name$ : e.$key$ === n.$key$);
      }, referenceNode = function(e) {
        // this node was relocated to a new location in the dom
        // because of some other component's slot
        // but we still have an html comment in place of where
        // it's original location was according to it's original vdom
        return e && e["s-ol"] || e;
      }, parentReferenceNode = function(e) {
        return (e["s-ol"] ? e["s-ol"] : e).parentNode;
      }, patch = function(e, n) {
        var t, r = n.$elm$ = e.$elm$, o = e.$children$, s = n.$children$, l = n.$tag$, a = n.$text$;
        null === a ? ("slot" === l || 
        // either this is the first render of an element OR it's an update
        // AND we already know it's possible it could have changed
        // this updates the element's css classes, attrs, props, listeners, etc.
        updateElement(e, n, !1), null !== o && null !== s ? 
        // looks like there's child vnodes for both the old and new vnodes
        function(e, n, t, r) {
          for (var o, s, l = 0, a = 0, $ = 0, i = 0, c = n.length - 1, u = n[0], f = n[c], d = r.length - 1, m = r[0], h = r[d]; l <= c && a <= d; ) if (null == u) 
          // Vnode might have been moved left
          u = n[++l]; else if (null == f) f = n[--c]; else if (null == m) m = r[++a]; else if (null == h) h = r[--d]; else if (isSameVnode(u, m)) patch(u, m), 
          u = n[++l], m = r[++a]; else if (isSameVnode(f, h)) patch(f, h), f = n[--c], h = r[--d]; else if (isSameVnode(u, h)) 
          // Vnode moved right
          "slot" !== u.$tag$ && "slot" !== h.$tag$ || putBackInOriginalLocation(u.$elm$.parentNode, !1), 
          patch(u, h), e.insertBefore(u.$elm$, f.$elm$.nextSibling), u = n[++l], h = r[--d]; else if (isSameVnode(f, m)) 
          // Vnode moved left
          "slot" !== u.$tag$ && "slot" !== h.$tag$ || putBackInOriginalLocation(f.$elm$.parentNode, !1), 
          patch(f, m), e.insertBefore(f.$elm$, u.$elm$), f = n[--c], m = r[++a]; else {
            for (
            // createKeyToOldIdx
            $ = -1, i = l; i <= c; ++i) if (n[i] && null !== n[i].$key$ && n[i].$key$ === m.$key$) {
              $ = i;
              break;
            }
            $ >= 0 ? ((s = n[$]).$tag$ !== m.$tag$ ? o = createElm(n && n[a], t, $, e) : (patch(s, m), 
            n[$] = void 0, o = s.$elm$), m = r[++a]) : (
            // new element
            o = createElm(n && n[a], t, a, e), m = r[++a]), o && parentReferenceNode(u.$elm$).insertBefore(o, referenceNode(u.$elm$));
          }
          l > c ? addVnodes(e, null == r[d + 1] ? null : r[d + 1].$elm$, t, r, a, d) : a > d && removeVnodes(n, l, c);
        }(r, o, n, s) : null !== s ? (
        // no old child vnodes, but there are new child vnodes to add
        null !== e.$text$ && (
        // the old vnode was text, so be sure to clear it out
        r.textContent = ""), 
        // add the new vnode children
        addVnodes(r, null, n, s, 0, s.length - 1)) : null !== o && 
        // no new child vnodes, but there are old child vnodes to remove
        removeVnodes(o, 0, o.length - 1)) : (t = r["s-cr"]) ? 
        // this element has slotted content
        t.parentNode.textContent = a : e.$text$ !== a && (
        // update the text content for the text only vnode
        // and also only if the text is different than before
        r.data = a);
      }, updateFallbackSlotVisibility = function(e) {
        // tslint:disable-next-line: prefer-const
        var n, t, r, o, s, l, a = e.childNodes;
        for (t = 0, r = a.length; t < r; t++) if (1 /* ElementNode */ === (n = a[t]).nodeType) {
          if (n["s-sr"]) for (
          // this is a slot fallback node
          // get the slot name for this slot reference node
          s = n["s-sn"], 
          // by default always show a fallback slot node
          // then hide it if there are other slots in the light dom
          n.hidden = !1, o = 0; o < r; o++) if (a[o]["s-hn"] !== n["s-hn"]) if (
          // this sibling node is from a different component
          l = a[o].nodeType, "" !== s) {
            // this is a named fallback slot node
            if (1 /* ElementNode */ === l && s === a[o].getAttribute("slot")) {
              n.hidden = !0;
              break;
            }
          } else 
          // this is a default fallback slot node
          // any element or text node (with content)
          // should hide the default fallback slot node
          if (1 /* ElementNode */ === l || 3 /* TextNode */ === l && "" !== a[o].textContent.trim()) {
            n.hidden = !0;
            break;
          }
          // keep drilling down
                    updateFallbackSlotVisibility(n);
        }
      }, x = [], relocateSlotContent = function(e) {
        for (
        // tslint:disable-next-line: prefer-const
        var n, t, r, o, s, l, a = 0, i = e.childNodes, c = i.length; a < c; a++) {
          if ((n = i[a])["s-sr"] && (t = n["s-cr"])) for (
          // first got the content reference comment node
          // then we got it's parent, which is where all the host content is in now
          r = t.parentNode.childNodes, o = n["s-sn"], l = r.length - 1; l >= 0; l--) (t = r[l])["s-cn"] || t["s-nr"] || t["s-hn"] === n["s-hn"] || (
          // let's do some relocating to its new home
          // but never relocate a content reference node
          // that is suppose to always represent the original content location
          isNodeLocatedInSlot(t, o) ? (
          // it's possible we've already decided to relocate this node
          s = x.find((function(e) {
            return e.$nodeToRelocate$ === t;
          })), 
          // made some changes to slots
          // let's make sure we also double check
          // fallbacks are correctly hidden or shown
          $ = !0, t["s-sn"] = t["s-sn"] || o, s ? 
          // previously we never found a slot home for this node
          // but turns out we did, so let's remember it now
          s.$slotRefNode$ = n : 
          // add to our list of nodes to relocate
          x.push({
            $slotRefNode$: n,
            $nodeToRelocate$: t
          }), t["s-sr"] && x.map((function(e) {
            isNodeLocatedInSlot(e.$nodeToRelocate$, t["s-sn"]) && (s = x.find((function(e) {
              return e.$nodeToRelocate$ === t;
            }))) && !e.$slotRefNode$ && (e.$slotRefNode$ = s.$slotRefNode$);
          }))) : x.some((function(e) {
            return e.$nodeToRelocate$ === t;
          })) || 
          // so far this element does not have a slot home, not setting slotRefNode on purpose
          // if we never find a home for this element then we'll need to hide it
          x.push({
            $nodeToRelocate$: t
          }));
          1 /* ElementNode */ === n.nodeType && relocateSlotContent(n);
        }
      }, isNodeLocatedInSlot = function(e, n) {
        return 1 /* ElementNode */ === e.nodeType ? null === e.getAttribute("slot") && "" === n || e.getAttribute("slot") === n : e["s-sn"] === n || "" === n;
      }, callNodeRefs = function(e) {
        e.$attrs$ && e.$attrs$.ref && e.$attrs$.ref(null), e.$children$ && e.$children$.map(callNodeRefs);
      }, renderVdom = function(e, n) {
        var s, l = e.$hostElement$, c = e.$cmpMeta$, u = e.$vnode$ || newVNode(null, null), f = (s = n) && s.$tag$ === _ ? n : N(null, null, n);
        if (o = l.tagName, c.$attrsToReflect$ && (f.$attrs$ = f.$attrs$ || {}, c.$attrsToReflect$.map((function(e) {
          var n = e[0], t = e[1];
          return f.$attrs$[t] = l[n];
        }))), f.$tag$ = null, f.$flags$ |= 4 /* isHost */ , e.$vnode$ = f, f.$elm$ = u.$elm$ = l.shadowRoot || l, 
        t = l["s-sc"], r = l["s-cr"], a = p && 0 != (1 /* shadowDomEncapsulation */ & c.$flags$), 
        // always reset
        $ = !1, 
        // synchronous patch
        patch(u, f), 
        // while we're moving nodes around existing nodes, temporarily disable
        // the disconnectCallback from working
        h.$flags$ |= 1 /* isTmpDisconnected */ , i) {
          relocateSlotContent(f.$elm$);
          for (var d = void 0, m = void 0, g = void 0, v = void 0, y = void 0, b = void 0, w = 0; w < x.length; w++) (m = (d = x[w]).$nodeToRelocate$)["s-ol"] || (
          // add a reference node marking this node's original location
          // keep a reference to this node for later lookups
          (g = originalLocationDebugNode(m))["s-nr"] = m, m.parentNode.insertBefore(m["s-ol"] = g, m));
          for (w = 0; w < x.length; w++) if (m = (d = x[w]).$nodeToRelocate$, d.$slotRefNode$) {
            for (
            // by default we're just going to insert it directly
            // after the slot reference node
            v = d.$slotRefNode$.parentNode, y = d.$slotRefNode$.nextSibling, g = m["s-ol"]; g = g.previousSibling; ) if ((b = g["s-nr"]) && b["s-sn"] === m["s-sn"] && v === b.parentNode && (!(b = b.nextSibling) || !b["s-nr"])) {
              y = b;
              break;
            }
            (!y && v !== m.parentNode || m.nextSibling !== y) && m !== y && (!m["s-hn"] && m["s-ol"] && (
            // probably a component in the index.html that doesn't have it's hostname set
            m["s-hn"] = m["s-ol"].parentNode.nodeName), 
            // add it back to the dom but in its new home
            v.insertBefore(m, y));
          } else 
          // this node doesn't have a slot home to go to, so let's hide it
          1 /* ElementNode */ === m.nodeType && (m.hidden = !0);
        }
        $ && updateFallbackSlotVisibility(f.$elm$), 
        // done moving nodes around
        // allow the disconnect callback to work again
        h.$flags$ &= -2 /* isTmpDisconnected */ , 
        // always reset
        x.length = 0;
      }, slotReferenceDebugNode = function(e) {
        return m.createComment("<slot" + (e.$name$ ? ' name="' + e.$name$ + '"' : "") + "> (host=" + o.toLowerCase() + ")");
      }, originalLocationDebugNode = function(e) {
        return m.createComment("org-location for " + (e.localName ? "<" + e.localName + "> (host=" + e["s-hn"] + ")" : "[" + e.textContent + "]"));
      }, C = e("g", (function(e) {
        return getHostRef(e).$hostElement$;
      })), k = (e("c", (function(e, n, t) {
        var r = C(e);
        return {
          emit: function(e) {
            return k(r, n, {
              bubbles: !!(4 /* Bubbles */ & t),
              composed: !!(2 /* Composed */ & t),
              cancelable: !!(1 /* Cancellable */ & t),
              detail: e
            });
          }
        };
      })), function(e, n, t) {
        var r = h.ce(n, t);
        return e.dispatchEvent(r), r;
      }), attachToAncestor = function(e, n) {
        n && !e.$onRenderResolve$ && n["s-p"] && n["s-p"].push(new Promise((function(n) {
          return e.$onRenderResolve$ = n;
        })));
      }, scheduleUpdate = function(e, n) {
        if (e.$flags$ |= 16 /* isQueuedForUpdate */ , !(4 /* isWaitingForChildren */ & e.$flags$)) {
          attachToAncestor(e, e.$ancestorComponent$);
          return O((function() {
            return dispatchHooks(e, n);
          }));
        }
        e.$flags$ |= 512 /* needsRerender */;
      }, dispatchHooks = function(e, n) {
        var t, r = e.$hostElement$, o = createTime(0, e.$cmpMeta$.$tagName$), s = e.$lazyInstance$;
        return n ? (e.$flags$ |= 256 /* isListenReady */ , e.$queuedListeners$ && (e.$queuedListeners$.map((function(e) {
          var n = e[0], t = e[1];
          return safeCall(s, n, t);
        })), e.$queuedListeners$ = null), emitLifecycleEvent(r, "componentWillLoad"), t = safeCall(s, "componentWillLoad")) : emitLifecycleEvent(r, "componentWillUpdate"), 
        emitLifecycleEvent(r, "componentWillRender"), o(), then(t, (function() {
          return updateComponent(e, s, n);
        }));
      }, updateComponent = function(e, n, t) {
        // updateComponent
        var r = e.$hostElement$, o = createTime(0, e.$cmpMeta$.$tagName$), s = r["s-rc"];
        t && 
        // DOM WRITE!
        attachStyles(e);
        var l = createTime(0, e.$cmpMeta$.$tagName$);
        // looks like we've got child nodes to render into this host element
        // or we need to update the css class/attrs on the host element
        // DOM WRITE!
        renderVdom(e, callRender(e, n)), h.$cssShim$ && h.$cssShim$.updateHost(r), s && (
        // ok, so turns out there are some child host elements
        // waiting on this parent element to load
        // let's fire off all update callbacks waiting
        s.map((function(e) {
          return e();
        })), r["s-rc"] = void 0), l(), o();
        var a = r["s-p"], postUpdate = function() {
          return L(e);
        };
        0 === a.length ? postUpdate() : (Promise.all(a).then(postUpdate), e.$flags$ |= 4 /* isWaitingForChildren */ , 
        a.length = 0);
      }, callRender = function(e, n) {
        try {
          c = n, n = n.render(), e.$flags$ &= -17 /* isQueuedForUpdate */ , e.$flags$ |= 2 /* hasRendered */;
        } catch (t) {
          consoleError(t);
        }
        return c = null, n;
      }, L = (e("f", (function() {
        return c;
      })), function(e) {
        var n = e.$cmpMeta$.$tagName$, t = e.$hostElement$, r = createTime(0, n), o = e.$lazyInstance$, s = e.$ancestorComponent$;
        emitLifecycleEvent(t, "componentDidRender"), 64 /* hasLoadedComponent */ & e.$flags$ ? (emitLifecycleEvent(t, "componentDidUpdate"), 
        r()) : (e.$flags$ |= 64 /* hasLoadedComponent */ , 
        // DOM WRITE!
        addHydratedFlag(t), safeCall(o, "componentDidLoad"), emitLifecycleEvent(t, "componentDidLoad"), 
        r(), e.$onReadyResolve$(t), s || appDidLoad()), e.$onInstanceResolve$(t), e.$onRenderResolve$ && (e.$onRenderResolve$(), 
        e.$onRenderResolve$ = void 0), 512 /* needsRerender */ & e.$flags$ && nextTick((function() {
          return scheduleUpdate(e, !1);
        })), e.$flags$ &= -517 /* needsRerender */;
      }), E = e("i", (function(e) {
        var n = getHostRef(e), t = n.$hostElement$.isConnected;
        // Returns "true" when the forced update was successfully scheduled
        return t && 2 /* hasRendered */ == (18 /* isQueuedForUpdate */ & n.$flags$) && scheduleUpdate(n, !1), 
        t;
      })), appDidLoad = function(e) {
        addHydratedFlag(m.documentElement), h.$flags$ |= 2 /* appLoaded */ , nextTick((function() {
          return k(d, "appload", {
            detail: {
              namespace: l
            }
          });
        }));
      }, safeCall = function(e, n, t) {
        if (e && e[n]) try {
          return e[n](t);
        } catch (r) {
          consoleError(r);
        }
      }, then = function(e, n) {
        return e && e.then ? e.then(n) : n();
      }, emitLifecycleEvent = function(e, n) {
        k(e, "stencil_" + n, {
          bubbles: !0,
          composed: !0,
          detail: {
            namespace: l
          }
        });
      }, addHydratedFlag = function(e) {
        return e.classList.add("hydrated");
      }, setValue = function(e, n, t, r) {
        // check our new property value against our internal value
        var o, s, l = getHostRef(e), a = l.$instanceValues$.get(n), $ = l.$flags$, i = l.$lazyInstance$;
        if (o = t, s = r.$members$[n][0], t = 
        // ensure this value is of the correct prop type
        null == o || isComplexType(o) ? o : 4 /* Boolean */ & s ? "false" !== o && ("" === o || !!o) : 2 /* Number */ & s ? parseFloat(o) : 1 /* String */ & s ? String(o) : o, 
        !(8 /* isConstructingInstance */ & $ && void 0 !== a || t === a) && (
        // gadzooks! the property's value has changed!!
        // set our new value!
        l.$instanceValues$.set(n, t), i)) {
          // get an array of method names of watch functions to call
          if (r.$watchers$ && 128 /* isWatchReady */ & $) {
            var c = r.$watchers$[n];
            c && 
            // this instance is watching for when this property changed
            c.map((function(e) {
              try {
                // fire off each of the watch methods that are watching this property
                i[e](t, a, n);
              } catch (r) {
                consoleError(r);
              }
            }));
          }
          2 /* hasRendered */ == (18 /* isQueuedForUpdate */ & $) && 
          // looks like this value actually changed, so we've got work to do!
          // but only if we've already rendered, otherwise just chill out
          // queue that we need to do an update, but don't worry about queuing
          // up millions cuz this function ensures it only runs once
          scheduleUpdate(l, !1);
        }
      }, proxyComponent = function(e, n, t) {
        if (n.$members$) {
          e.watchers && (n.$watchers$ = e.watchers);
          // It's better to have a const than two Object.entries()
                    var r = Object.entries(n.$members$), o = e.prototype;
          if (r.map((function(e) {
            var r = e[0], s = e[1][0];
            31 /* Prop */ & s || 2 /* proxyState */ & t && 32 /* State */ & s ? 
            // proxyComponent - prop
            Object.defineProperty(o, r, {
              get: function() {
                // proxyComponent, get value
                return e = r, getHostRef(this).$instanceValues$.get(e);
                var e;
              },
              set: function(e) {
                // proxyComponent, set value
                setValue(this, r, e, n);
              },
              configurable: !0,
              enumerable: !0
            }) : 1 /* isElementConstructor */ & t && 64 /* Method */ & s && 
            // proxyComponent - method
            Object.defineProperty(o, r, {
              value: function() {
                for (var e = [], n = 0; n < arguments.length; n++) e[n] = arguments[n];
                var t = getHostRef(this);
                return t.$onInstancePromise$.then((function() {
                  var n;
                  return (n = t.$lazyInstance$)[r].apply(n, e);
                }));
              }
            });
          })), 1 /* isElementConstructor */ & t) {
            var s = new Map;
            o.attributeChangedCallback = function(e, n, t) {
              var r = this;
              h.jmp((function() {
                var n = s.get(e);
                r[n] = (null !== t || "boolean" != typeof r[n]) && t;
              }));
            }, 
            // create an array of attributes to observe
            // and also create a map of html attribute name to js property name
            e.observedAttributes = r.filter((function(e) {
              e[0];
              return 15 & e[1][0];
            } /* HasAttribute */)).map((function(e) {
              var t = e[0], r = e[1], o = r[1] || t;
              return s.set(o, t), 512 /* ReflectAttr */ & r[0] && n.$attrsToReflect$.push([ t, o ]), 
              o;
            }));
          }
        }
        return e;
      }, initializeComponent = function(e, t, r, o, l) {
        return __awaiter(s, void 0, void 0, (function() {
          var e, o, s, a, $, i, c;
          return __generator(this, (function(u) {
            switch (u.label) {
             case 0:
              return 0 != (32 /* hasInitializedComponent */ & t.$flags$) ? [ 3 /*break*/ , 5 ] : (
              // we haven't initialized this element yet
              t.$flags$ |= 32 /* hasInitializedComponent */ , (
              // lazy loaded components
              // request the component's implementation to be
              // wired up with the host element
              l = loadModule(r)).then ? (e = function() {}, [ 4 /*yield*/ , l ]) : [ 3 /*break*/ , 2 ]);

             case 1:
              l = u.sent(), e(), u.label = 2;

             case 2:
              if (!l) throw new Error('Constructor for "' + r.$tagName$ + "#" + t.$modeName$ + '" was not found');
              l.isProxied || (r.$watchers$ = l.watchers, proxyComponent(l, r, 2 /* proxyState */), 
              l.isProxied = !0), o = createTime(0, r.$tagName$), t.$flags$ |= 8 /* isConstructingInstance */;
              // construct the lazy-loaded component implementation
              // passing the hostRef is very important during
              // construction in order to directly wire together the
              // host element and the lazy-loaded instance
              try {
                new l(t);
              } catch (f) {
                consoleError(f);
              }
              return t.$flags$ &= -9 /* isConstructingInstance */ , t.$flags$ |= 128 /* isWatchReady */ , 
              o(), fireConnectedCallback(t.$lazyInstance$), l.style ? (s = l.style, a = getScopeId(r), 
              M.has(a) ? [ 3 /*break*/ , 5 ] : ($ = createTime(0, r.$tagName$), 8 /* needsShadowDomShim */ & r.$flags$ ? [ 4 /*yield*/ , n.import("./p-50811587.system.js").then((function(e) {
                return e.scopeCss(s, a, !1);
              })) ] : [ 3 /*break*/ , 4 ] /* needsShadowDomShim */)) : [ 3 /*break*/ , 5 ];

             case 3:
              s = u.sent(), u.label = 4;

             case 4:
              !function(e, n, t) {
                var r = M.get(e);
                v && t ? (r = r || new CSSStyleSheet).replace(n) : r = n, M.set(e, r);
              }(a, s, !!(1 /* shadowDomEncapsulation */ & r.$flags$)), $(), u.label = 5;

             case 5:
              return i = t.$ancestorComponent$, c = function() {
                return scheduleUpdate(t, !0);
              }, i && i["s-rc"] ? 
              // this is the intial load and this component it has an ancestor component
              // but the ancestor component has NOT fired its will update lifecycle yet
              // so let's just cool our jets and wait for the ancestor to continue first
              // this will get fired off when the ancestor component
              // finally gets around to rendering its lazy self
              // fire off the initial update
              i["s-rc"].push(c) : c(), [ 2 /*return*/ ];
            }
          }));
        }));
      }, fireConnectedCallback = function(e) {
        safeCall(e, "connectedCallback");
      }, setContentReference = function(e) {
        // only required when we're NOT using native shadow dom (slot)
        // or this browser doesn't support native shadow dom
        // and this host element was NOT created with SSR
        // let's pick out the inner content for slot projection
        // create a node to represent where the original
        // content was first placed, which is useful later on
        var n = e["s-cr"] = m.createComment("content-ref (host=" + e.localName + ")");
        n["s-cn"] = !0, e.insertBefore(n, e.firstChild);
      }, T = (e("b", (function(e, n) {
        void 0 === n && (n = {});
        var t, r = createTime(), o = [], s = n.exclude || [], l = d.customElements, a = m.head, $ =  a.querySelector("meta[charset]"), i =  m.createElement("style"), c = [], u = !0;
        Object.assign(h, n), h.$resourcesUrl$ = new URL(n.resourcesUrl || "./", m.baseURI).href, 
        n.syncQueue && (h.$flags$ |= 4 /* queueSync */), e.map((function(e) {
          return e[1].map((function(n) {
            var r = {
              $flags$: n[0],
              $tagName$: n[1],
              $members$: n[2],
              $listeners$: n[3]
            };
            r.$members$ = n[2], r.$listeners$ = n[3], r.$attrsToReflect$ = [], r.$watchers$ = {}, 
            !p && 1 /* shadowDomEncapsulation */ & r.$flags$ && (r.$flags$ |= 8 /* needsShadowDomShim */);
            var a = r.$tagName$, $ = /** @class */ function(e) {
              // StencilLazyHost
              function HostElement(n) {
                var t = 
                // @ts-ignore
                e.call(this, n) || this;
                return j(n = t, r), 1 /* shadowDomEncapsulation */ & r.$flags$ && (
                // this component is using shadow dom
                // and this browser supports shadow dom
                // add the read-only property "shadowRoot" to the host element
                // adding the shadow root build conditionals to minimize runtime
                p ? n.attachShadow({
                  mode: "open"
                }) : "shadowRoot" in n || (n.shadowRoot = n)), t;
              }
              return __extends(HostElement, e), HostElement.prototype.connectedCallback = function() {
                var e = this;
                t && (clearTimeout(t), t = null), u ? 
                // connectedCallback will be processed once all components have been registered
                c.push(this) : h.jmp((function() {
                  return function(e) {
                    if (0 == (1 /* isTmpDisconnected */ & h.$flags$)) {
                      var n = getHostRef(e), t = n.$cmpMeta$, r = createTime(0, t.$tagName$);
                      if (1 /* hasConnected */ & n.$flags$) 
                      // not the first time this has connected
                      // reattach any event listeners to the host
                      // since they would have been removed when disconnected
                      addHostEventListeners(e, n, t.$listeners$), 
                      // fire off connectedCallback() on component instance
                      fireConnectedCallback(n.$lazyInstance$); else {
                        // first time this component has connected
                        n.$flags$ |= 1 /* hasConnected */ , 
                        // initUpdate
                        // if the slot polyfill is required we'll need to put some nodes
                        // in here to act as original content anchors as we move nodes around
                        // host element has been connected to the DOM
                        12 /* needsShadowDomShim */ & t.$flags$ && setContentReference(e);
                        for (
                        // find the first ancestor component (if there is one) and register
                        // this component as one of the actively loading child components for its ancestor
                        var o = e; o = o.parentNode || o.host; ) 
                        // climb up the ancestors looking for the first
                        // component that hasn't finished its lifecycle update yet
                        if (o["s-p"]) {
                          // we found this components first ancestor component
                          // keep a reference to this component's ancestor component
                          attachToAncestor(n, n.$ancestorComponent$ = o);
                          break;
                        }
                        // Lazy properties
                        // https://developers.google.com/web/fundamentals/web-components/best-practices#lazy-properties
                        t.$members$ && Object.entries(t.$members$).map((function(n) {
                          var t = n[0];
                          if (31 /* Prop */ & n[1][0] && e.hasOwnProperty(t)) {
                            var r = e[t];
                            delete e[t], e[t] = r;
                          }
                        })), 
                        // connectedCallback, taskQueue, initialLoad
                        // angular sets attribute AFTER connectCallback
                        // https://github.com/angular/angular/issues/18909
                        // https://github.com/angular/angular/issues/19940
                        nextTick((function() {
                          return initializeComponent(0, n, t);
                        }));
                      }
                      r();
                    }
                  }(e);
                }));
              }, HostElement.prototype.disconnectedCallback = function() {
                var e = this;
                h.jmp((function() {
                  return function(e) {
                    if (0 == (1 /* isTmpDisconnected */ & h.$flags$)) {
                      var n = getHostRef(e), t = n.$lazyInstance$;
                      n.$rmListeners$ && (n.$rmListeners$.map((function(e) {
                        return e();
                      })), n.$rmListeners$ = void 0), 
                      // clear CSS var-shim tracking
                      h.$cssShim$ && h.$cssShim$.removeHost(e), safeCall(t, "disconnectedCallback"), safeCall(t, "componentDidUnload");
                    }
                  }(e);
                }));
              }, HostElement.prototype.forceUpdate = function() {
                E(this);
              }, HostElement.prototype.componentOnReady = function() {
                return getHostRef(this).$onReadyPromise$;
              }, HostElement;
            }(HTMLElement);
            r.$lazyBundleId$ = e[0], s.includes(a) || l.get(a) || (o.push(a), l.define(a, proxyComponent($, r, 1 /* isElementConstructor */)));
          }));
        })), i.innerHTML = o + "{visibility:hidden}.hydrated{visibility:inherit}", i.setAttribute("data-styles", ""), 
        a.insertBefore(i, $ ? $.nextSibling : a.firstChild), 
        // Process deferred connectedCallbacks now all components have been registered
        u = !1, c.length ? c.map((function(e) {
          return e.connectedCallback();
        })) : h.jmp((function() {
          return t = setTimeout(appDidLoad, 30);
        })), 
        // Fallback appLoad event
        r();
      })), e("e", (function(e) {
        var n = new URL(e, h.$resourcesUrl$);
        return n.origin !== d.location.origin ? n.href : n.pathname;
      })), new WeakMap), getHostRef = function(e) {
        return T.get(e);
      }, j = (e("r", (function(e, n) {
        return T.set(n.$lazyInstance$ = e, n);
      })), function(e, n) {
        var t = {
          $flags$: 0,
          $hostElement$: e,
          $cmpMeta$: n,
          $instanceValues$: new Map
        };
        return t.$onInstancePromise$ = new Promise((function(e) {
          return t.$onInstanceResolve$ = e;
        })), t.$onReadyPromise$ = new Promise((function(e) {
          return t.$onReadyResolve$ = e;
        })), e["s-p"] = [], e["s-rc"] = [], addHostEventListeners(e, t, n.$listeners$), 
        T.set(e, t);
      }), isMemberInElement = function(e, n) {
        return n in e;
      }, consoleError = function(e) {
        return console.error(e);
      }, P =  new Map, loadModule = function(e, t, r) {
        // loadModuleImport
        var o = e.$tagName$.replace(/-/g, "_"), s = e.$lazyBundleId$, l = P.get(s);
        return l ? l[o] : n.import(
        /* webpackInclude: /\.entry\.js$/ */
        /* webpackExclude: /\.system\.entry\.js$/ */
        /* webpackMode: "lazy" */
        "./" + s + ".entry.js").then((function(e) {
          return P.set(s, e), e[o];
        }), consoleError);
      }, M = new Map, A = [], I = [], H = [], queueTask = function(e, n) {
        return function(t) {
          e.push(t), f || (f = !0, n && 4 /* queueSync */ & h.$flags$ ? nextTick(flush) : h.raf(flush));
        };
      }, consumeTimeout = function(e, n) {
        for (var t = 0, r = 0; t < e.length && (r = performance.now()) < n; ) try {
          e[t++](r);
        } catch (o) {
          consoleError(o);
        }
        t === e.length ? e.length = 0 : 0 !== t && e.splice(0, t);
      }, flush = function() {
        u++, 
        // always force a bunch of medium callbacks to run, but still have
        // a throttle on how many can run in a certain time
        // DOM READS!!!
        function(e) {
          for (var n = 0; n < e.length; n++) try {
            e[n](performance.now());
          } catch (t) {
            consoleError(t);
          }
          e.length = 0;
        }(A);
        var e = 2 /* appLoaded */ == (6 /* queueMask */ & h.$flags$) ? performance.now() + 14 * Math.ceil(.1 * u) : 1 / 0;
        consumeTimeout(I, e), consumeTimeout(H, e), I.length > 0 && (H.push.apply(H, I), 
        I.length = 0), (f = A.length + I.length + H.length > 0) ? 
        // still more to do yet, but we've run out of time
        // let's let this thing cool off and try again in the next tick
        h.raf(flush) : u = 0;
      }, nextTick =  function(e) {
        return g().then(e);
      }, O =  queueTask(I, !0);
    }
  };
}));