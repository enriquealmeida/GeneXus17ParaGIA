var __awaiter = this && this.__awaiter || function(t, e, i, r) {
  return new (i || (i = Promise))((function(s, o) {
    function fulfilled(t) {
      try {
        step(r.next(t));
      } catch (e) {
        o(e);
      }
    }
    function rejected(t) {
      try {
        step(r.throw(t));
      } catch (e) {
        o(e);
      }
    }
    function step(t) {
      var e;
      t.done ? s(t.value) : (e = t.value, e instanceof i ? e : new i((function(t) {
        t(e);
      }))).then(fulfilled, rejected);
    }
    step((r = r.apply(t, e || [])).next());
  }));
}, __generator = this && this.__generator || function(t, e) {
  var i, r, s, o, n = {
    label: 0,
    sent: function() {
      if (1 & s[0]) throw s[1];
      return s[1];
    },
    trys: [],
    ops: []
  };
  return o = {
    next: verb(0),
    throw: verb(1),
    return: verb(2)
  }, "function" == typeof Symbol && (o[Symbol.iterator] = function() {
    return this;
  }), o;
  function verb(o) {
    return function(l) {
      return function(o) {
        if (i) throw new TypeError("Generator is already executing.");
        for (;n; ) try {
          if (i = 1, r && (s = 2 & o[0] ? r.return : o[0] ? r.throw || ((s = r.return) && s.call(r), 
          0) : r.next) && !(s = s.call(r, o[1])).done) return s;
          switch (r = 0, s && (o = [ 2 & o[0], s.value ]), o[0]) {
           case 0:
           case 1:
            s = o;
            break;

           case 4:
            return n.label++, {
              value: o[1],
              done: !1
            };

           case 5:
            n.label++, r = o[1], o = [ 0 ];
            continue;

           case 7:
            o = n.ops.pop(), n.trys.pop();
            continue;

           default:
            if (!(s = n.trys, (s = s.length > 0 && s[s.length - 1]) || 6 !== o[0] && 2 !== o[0])) {
              n = 0;
              continue;
            }
            if (3 === o[0] && (!s || o[1] > s[0] && o[1] < s[3])) {
              n.label = o[1];
              break;
            }
            if (6 === o[0] && n.label < s[1]) {
              n.label = s[1], s = o;
              break;
            }
            if (s && n.label < s[2]) {
              n.label = s[2], n.ops.push(o);
              break;
            }
            s[2] && n.ops.pop(), n.trys.pop();
            continue;
          }
          o = e.call(t, n);
        } catch (l) {
          o = [ 6, l ], r = 0;
        } finally {
          i = s = 0;
        }
        if (5 & o[0]) throw o[1];
        return {
          value: o[0] ? o[1] : void 0,
          done: !0
        };
      }([ o, l ]);
    };
  }
};

System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, i, r, s, o;
  return {
    setters: [ function(t) {
      e = t.r, i = t.h, r = t.H, s = t.c, o = t.g;
    } ],
    execute: function() {
      t("gxg_split", /** @class */ function() {
        function class_1(t) {
          e(this, t);
        }
        return class_1.prototype.render = function() {
          return i(r, null, i("slot", null));
        }, class_1;
      }()).style = ":host{display:block;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;overflow-y:hidden;overflow-x:hidden;background-color:var(--color-background)}:host(.split-horizontal){height:100%;float:left}:host(.smooth-transition){-webkit-transition:width 0.35s, height 0.35s;transition:width 0.35s, height 0.35s;-webkit-transition-timing-function:ease-in-out;transition-timing-function:ease-in-out}";
      // The programming goals of Split.js are to deliver readable, understandable and
      // maintainable code, while at the same time manually optimizing for tiny minified file size,
      // browser compatibility without additional requirements
      // and very few assumptions about the user's page layout.
      var n = "undefined" != typeof window ? window : null, l = null === n, a = l ? void 0 : n.document, NOOP = function() {
        return !1;
      }, c = l ? "calc" : [ "", "-webkit-", "-moz-", "-o-" ].filter((function(t) {
        var e = a.createElement("div");
        return e.style.cssText = "width:" + t + "calc(9px)", !!e.style.length;
      })).shift() + "calc", isString = function(t) {
        return "string" == typeof t || t instanceof String;
      }, elementOrSelector = function(t) {
        if (isString(t)) {
          var e = a.querySelector(t);
          if (!e) throw new Error("Selector " + t + " did not match a DOM element");
          return e;
        }
        return t;
      }, getOption = function(t, e, i) {
        var r = t[e];
        return void 0 !== r ? r : i;
      }, getGutterSize = function(t, e, i, r) {
        if (e) {
          if ("end" === r) return 0;
          if ("center" === r) return t / 2;
        } else if (i) {
          if ("start" === r) return 0;
          if ("center" === r) return t / 2;
        }
        return t;
      }, defaultGutterFn = function(t, e) {
        var i = a.createElement("div");
        return i.className = "gutter gutter-" + e, i;
      }, defaultElementStyleFn = function(t, e, i) {
        var r = {};
        return isString(e) ? r[t] = e : r[t] = c + "(" + e + "% - " + i + "px)", r;
      }, defaultGutterStyleFn = function(t, e) {
        var i;
        return (i = {})[t] = e + "px", i;
      }, Split = function(t, e) {
        if (void 0 === e && (e = {}), l) return {};
        var i, r, s, o, c, h, d = t;
        // Allow HTMLCollection to be used as an argument when supported
        Array.from && (d = Array.from(d));
        // All DOM elements in the split should have a common parent. We can grab
        // the first elements parent and hope users read the docs because the
        // behavior will be whacky otherwise.
                var u = elementOrSelector(d[0]).parentNode, p = getComputedStyle ? getComputedStyle(u) : null, g = p ? p.flexDirection : null, f = getOption(e, "sizes") || d.map((function() {
          return 100 / d.length;
        })), v = getOption(e, "minSize", 100), y = Array.isArray(v) ? v : d.map((function() {
          return v;
        })), m = getOption(e, "maxSize", 1 / 0), b = Array.isArray(m) ? m : d.map((function() {
          return m;
        })), z = getOption(e, "expandToMin", !1), S = getOption(e, "gutterSize", 10), x = getOption(e, "gutterAlign", "center"), k = getOption(e, "snapOffset", 30), E = getOption(e, "dragInterval", 1), _ = getOption(e, "direction", "horizontal"), w = getOption(e, "cursor", "horizontal" === _ ? "col-resize" : "row-resize"), L = getOption(e, "gutter", defaultGutterFn), M = getOption(e, "elementStyle", defaultElementStyleFn), R = getOption(e, "gutterStyle", defaultGutterStyleFn);
        // 3. Define the dragging helper functions, and a few helpers to go with them.
        // Each helper is bound to a pair object that contains its metadata. This
        // also makes it easy to store references to listeners that that will be
        // added and removed.
        // Even though there are no other functions contained in them, aliasing
        // this to self saves 50 bytes or so since it's used so frequently.
        // The pair object saves metadata like dragging state, position and
        // event listener references.
        function setElementSize(t, e, r, s) {
          // Split.js allows setting sizes via numbers (ideally), or if you must,
          // by string, like '300px'. This is less than ideal, because it breaks
          // the fluid layout that `calc(% - px)` provides. You're on your own if you do that,
          // make sure you calculate the gutter size by hand.
          var o = M(i, e, r, s);
          Object.keys(o).forEach((function(e) {
            // eslint-disable-next-line no-param-reassign
            t.style[e] = o[e];
          }));
        }
        function getSizes() {
          return h.map((function(t) {
            return t.size;
          }));
        }
        // Supports touch events, but not multitouch, so only the first
        // finger `touches[0]` is counted.
                function getMousePosition(t) {
          return "touches" in t ? t.touches[0][r] : t[r];
        }
        // Actually adjust the size of elements `a` and `b` to `offset` while dragging.
        // calc is used to allow calc(percentage + gutterpx) on the whole split instance,
        // which allows the viewport to be resized without additional logic.
        // Element a's size is the same as offset. b's size is total size - a size.
        // Both sizes are calculated from the initial parent percentage,
        // then the gutter size is subtracted.
                function adjust(t) {
          var e = h[this.a], i = h[this.b], r = e.size + i.size;
          e.size = t / this.size * r, i.size = r - t / this.size * r, setElementSize(e.element, e.size, this._b, e.i), 
          setElementSize(i.element, i.size, this._c, i.i);
        }
        // drag, where all the magic happens. The logic is really quite simple:
        
        // 1. Ignore if the pair is not dragging.
        // 2. Get the offset of the event.
        // 3. Snap offset to min if within snappable range (within min + snapOffset).
        // 4. Actually adjust each element in the pair to offset.
        
        // ---------------------------------------------------------------------
        // |    | <- a.minSize               ||              b.minSize -> |    |
        // |    |  | <- this.snapOffset      ||     this.snapOffset -> |  |    |
        // |    |  |                         ||                        |  |    |
        // |    |  |                         ||                        |  |    |
        // ---------------------------------------------------------------------
        // | <- this.start                                        this.size -> |
                function drag(t) {
          var i, r = h[this.a], s = h[this.b];
          this.dragging && (
          // Get the offset of the event from the first side of the
          // pair `this.start`. Then offset by the initial position of the
          // mouse compared to the gutter size.
          i = getMousePosition(t) - this.start + (this._b - this.dragOffset), E > 1 && (i = Math.round(i / E) * E), 
          // If within snapOffset of min or max, set offset to min or max.
          // snapOffset buffers a.minSize and b.minSize, so logic is opposite for both.
          // Include the appropriate gutter sizes to prevent overflows.
          i <= r.minSize + k + this._b ? i = r.minSize + this._b : i >= this.size - (s.minSize + k + this._c) && (i = this.size - (s.minSize + this._c)), 
          i >= r.maxSize - k + this._b ? i = r.maxSize + this._b : i <= this.size - (s.maxSize - k + this._c) && (i = this.size - (s.maxSize + this._c)), 
          // Actually adjust the size.
          adjust.call(this, i), 
          // Call the drag callback continously. Don't do anything too intensive
          // in this callback.
          getOption(e, "onDrag", NOOP)(getSizes()));
        }
        // Cache some important sizes when drag starts, so we don't have to do that
        // continously:
        
        // `size`: The total size of the pair. First + second + first gutter + second gutter.
        // `start`: The leading side of the first element.
        
        // ------------------------------------------------
        // |      aGutterSize -> |||                      |
        // |                     |||                      |
        // |                     |||                      |
        // |                     ||| <- bGutterSize       |
        // ------------------------------------------------
        // | <- start                             size -> |
                function calculateSizes() {
          // Figure out the parent size minus padding.
          var t = h[this.a].element, e = h[this.b].element, r = t.getBoundingClientRect(), n = e.getBoundingClientRect();
          this.size = r[i] + n[i] + this._b + this._c, this.start = r[s], this.end = r[o];
        }
        // When specifying percentage sizes that are less than the computed
        // size of the element minus the gutter, the lesser percentages must be increased
        // (and decreased from the other elements) to make space for the pixels
        // subtracted by the gutters.
        function trimToMin(t) {
          // Try to get inner size of parent element.
          // If it's no supported, return original sizes.
          var e = function(t) {
            // Return nothing if getComputedStyle is not supported (< IE9)
            // Or if parent element has no layout yet
            if (!getComputedStyle) return null;
            var e = getComputedStyle(t);
            if (!e) return null;
            var i = t[c];
            return 0 === i ? null : i -= "horizontal" === _ ? parseFloat(e.paddingLeft) + parseFloat(e.paddingRight) : parseFloat(e.paddingTop) + parseFloat(e.paddingBottom);
          }(u);
          if (null === e) return t;
          if (y.reduce((function(t, e) {
            return t + e;
          }), 0) > e) return t;
          // Keep track of the excess pixels, the amount of pixels over the desired percentage
          // Also keep track of the elements with pixels to spare, to decrease after if needed
                    var i = 0, r = [], s = t.map((function(s, o) {
            // Convert requested percentages to pixel sizes
            var n = e * s / 100, l = getGutterSize(S, 0 === o, o === t.length - 1, x), a = y[o] + l;
            // If element is too smal, increase excess pixels by the difference
            // and mark that it has no pixels to spare
            return n < a ? (i += a - n, r.push(0), a) : (
            // Otherwise, mark the pixels it has to spare and return it's original size
            r.push(n - a), n);
          }));
          // If nothing was adjusted, return the original sizes
          return 0 === i ? t : s.map((function(t, s) {
            var o = t;
            // While there's still pixels to take, and there's enough pixels to spare,
            // take as many as possible up to the total excess pixels
                        if (i > 0 && r[s] - i > 0) {
              var n = Math.min(i, r[s] - i);
              // Subtract the amount taken for the next iteration
                            i -= n, o = t - n;
            }
            // Return the pixel size adjusted as a percentage
                        return o / e * 100;
          }));
        }
        // stopDragging is very similar to startDragging in reverse.
                function stopDragging() {
          var t = h[this.a].element, i = h[this.b].element;
          this.dragging && getOption(e, "onDragEnd", NOOP)(getSizes()), this.dragging = !1, 
          // Remove the stored event listeners. This is why we store them.
          n.removeEventListener("mouseup", this.stop), n.removeEventListener("touchend", this.stop), 
          n.removeEventListener("touchcancel", this.stop), n.removeEventListener("mousemove", this.move), 
          n.removeEventListener("touchmove", this.move), 
          // Clear bound function references
          this.stop = null, this.move = null, t.removeEventListener("selectstart", NOOP), 
          t.removeEventListener("dragstart", NOOP), i.removeEventListener("selectstart", NOOP), 
          i.removeEventListener("dragstart", NOOP), t.style.userSelect = "", t.style.webkitUserSelect = "", 
          t.style.MozUserSelect = "", t.style.pointerEvents = "", i.style.userSelect = "", 
          i.style.webkitUserSelect = "", i.style.MozUserSelect = "", i.style.pointerEvents = "", 
          this.gutter.style.cursor = "", this.parent.style.cursor = "", a.body.style.cursor = "";
        }
        // startDragging calls `calculateSizes` to store the inital size in the pair object.
        // It also adds event listeners for mouse/touch events,
        // and prevents selection while dragging so avoid the selecting text.
                function startDragging(t) {
          // Right-clicking can't start dragging.
          if (!("button" in t) || 0 === t.button) {
            // Alias frequently used variables to save space. 200 bytes.
            var i = h[this.a].element, r = h[this.b].element;
            // Call the onDragStart callback.
            this.dragging || getOption(e, "onDragStart", NOOP)(getSizes()), 
            // Don't actually drag the element. We emulate that in the drag function.
            t.preventDefault(), 
            // Set the dragging property of the pair object.
            this.dragging = !0, 
            // Create two event listeners bound to the same pair object and store
            // them in the pair object.
            this.move = drag.bind(this), this.stop = stopDragging.bind(this), 
            // All the binding. `window` gets the stop events in case we drag out of the elements.
            n.addEventListener("mouseup", this.stop), n.addEventListener("touchend", this.stop), 
            n.addEventListener("touchcancel", this.stop), n.addEventListener("mousemove", this.move), 
            n.addEventListener("touchmove", this.move), 
            // Disable selection. Disable!
            i.addEventListener("selectstart", NOOP), i.addEventListener("dragstart", NOOP), 
            r.addEventListener("selectstart", NOOP), r.addEventListener("dragstart", NOOP), 
            i.style.userSelect = "none", i.style.webkitUserSelect = "none", i.style.MozUserSelect = "none", 
            i.style.pointerEvents = "none", r.style.userSelect = "none", r.style.webkitUserSelect = "none", 
            r.style.MozUserSelect = "none", r.style.pointerEvents = "none", 
            // Set the cursor at multiple levels
            this.gutter.style.cursor = w, this.parent.style.cursor = w, a.body.style.cursor = w, 
            // Cache the initial sizes of the pair.
            calculateSizes.call(this), 
            // Determine the position of the mouse compared to the gutter
            this.dragOffset = getMousePosition(t) - this.end;
          }
        }
        // adjust sizes to ensure percentage is within min size and gutter.
                // 2. Initialize a bunch of strings based on the direction we're splitting.
        // A lot of the behavior in the rest of the library is paramatized down to
        // rely on CSS strings and classes.
        "horizontal" === _ ? (i = "width", r = "clientX", s = "left", o = "right", c = "clientWidth") : "vertical" === _ && (i = "height", 
        r = "clientY", s = "top", o = "bottom", c = "clientHeight"), f = trimToMin(f);
        // 5. Create pair and element objects. Each pair has an index reference to
        // elements `a` and `b` of the pair (first and second elements).
        // Loop through the elements while pairing them off. Every pair gets a
        // `pair` object and a gutter.
        // Basic logic:
        // - Starting with the second element `i > 0`, create `pair` objects with
        //   `a = i - 1` and `b = i`
        // - Set gutter sizes based on the _pair_ being first/last. The first and last
        //   pair have gutterSize / 2, since they only have one half gutter, and not two.
        // - Create gutter elements and add event listeners.
        // - Set the size of the elements, minus the gutter sizes.
        // -----------------------------------------------------------------------
        // |     i=0     |         i=1         |        i=2       |      i=3     |
        // |             |                     |                  |              |
        // |           pair 0                pair 1             pair 2           |
        // |             |                     |                  |              |
        // -----------------------------------------------------------------------
        var A = [];
        function adjustToMin(t) {
          var e = t.i === A.length, i = e ? A[t.i - 1] : A[t.i];
          calculateSizes.call(i);
          var r = e ? i.size - t.minSize - i._c : t.minSize + i._b;
          adjust.call(i, r);
        }
        return (h = d.map((function(t, e) {
          // Create the element object.
          var r, s = {
            element: elementOrSelector(t),
            size: f[e],
            minSize: y[e],
            maxSize: b[e],
            i: e
          };
          if (e > 0 && (
          // Create the pair object with its metadata.
          (r = {
            a: e - 1,
            b: e,
            dragging: !1,
            direction: _,
            parent: u
          })._b = getGutterSize(S, e - 1 == 0, !1, x), r._c = getGutterSize(S, !1, e === d.length - 1, x), 
          "row-reverse" === g || "column-reverse" === g)) {
            var o = r.a;
            r.a = r.b, r.b = o;
          }
          // Determine the size of the current element. IE8 is supported by
          // staticly assigning sizes without draggable gutters. Assigns a string
          // to `size`.
          
          // Create gutter elements for each pair.
                    if (e > 0) {
            var n = L(e, _, s.element);
            !function(t, e, r) {
              var s = R(i, e, r);
              Object.keys(s).forEach((function(e) {
                // eslint-disable-next-line no-param-reassign
                t.style[e] = s[e];
              }));
            }(n, S, e), 
            // Save bound event listener for removal later
            r._a = startDragging.bind(r), 
            // Attach bound event listener
            n.addEventListener("mousedown", r._a), n.addEventListener("touchstart", r._a), u.insertBefore(n, s.element), 
            r.gutter = n;
          }
          return setElementSize(s.element, s.size, getGutterSize(S, 0 === e, e === d.length - 1, x), e), 
          // After the first iteration, and we have a pair object, append it to the
          // list of pairs.
          e > 0 && A.push(r), s;
        }))).forEach((function(t) {
          var e = t.element.getBoundingClientRect()[i];
          e < t.minSize && (z ? adjustToMin(t) : 
          // eslint-disable-next-line no-param-reassign
          t.minSize = e);
        })), {
          setSizes: function(t) {
            var e = trimToMin(t);
            e.forEach((function(t, i) {
              if (i > 0) {
                var r = A[i - 1], s = h[r.a], o = h[r.b];
                s.size = e[i - 1], o.size = t, setElementSize(s.element, s.size, r._b, s.i), setElementSize(o.element, o.size, r._c, o.i);
              }
            }));
          },
          getSizes: getSizes,
          collapse: function(t) {
            adjustToMin(h[t]);
          },
          destroy: function(t, e) {
            A.forEach((function(r) {
              if (!0 !== e ? r.parent.removeChild(r.gutter) : (r.gutter.removeEventListener("mousedown", r._a), 
              r.gutter.removeEventListener("touchstart", r._a)), !0 !== t) {
                var s = M(i, r.a.size, r._b);
                Object.keys(s).forEach((function(t) {
                  h[r.a].element.style[t] = "", h[r.b].element.style[t] = "";
                }));
              }
            }));
          },
          parent: u,
          pairs: A
        };
      };
      t("gxg_splitter", /** @class */ function() {
        function class_2(t) {
          e(this, t), this.dragging = s(this, "dragging", 7), this.dragEnded = s(this, "dragEnded", 7), 
          /**
                     * The splitter direction
                     */
          this.direction = "horizontal", 
          /**
                     * The splitter direction
                     */
          this.forceCollapseZero = !1, 
          /**
                     * The type of knob
                     */
          this.knob = "none", 
          /**
                     * The splitter min. sizes in pixels
                     */
          this.minSize = "0,0", 
          /**
                     * The splitter initial sizes, in percentages. The sum should equal 100
                     */
          this.sizes = "50,50", this.idsArray = [], this.leftSplitReachedMin = !1, this.rightSplitReachedMin = !1, 
          this.collapsedToZero = !1;
        }
        return class_2.prototype.makeId = function(t) {
          for (var e = [], i = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", r = i.length, s = 0; s < t; s++) e.push(i.charAt(Math.floor(Math.random() * r)));
          // this prevents the id begining with a number, which is not allowed
          return e.unshift("a"), e.join("");
        }, class_2.prototype.componentWillLoad = function() {
          this.el.querySelectorAll("gxg-split").forEach(function(t) {
            var e = this.makeId(5);
            t.setAttribute("id", e);
          }.bind(this));
        }, class_2.prototype.componentDidLoad = function() {
          var t = this;
          this.getIds(), this.convertStringPropertiesToArray(), this.validateSizes(), this.split = Split(this.idsArray, {
            gutterSize: 8,
            cursor: "col-resize",
            minSize: this.minSizeArray,
            sizes: this.sizesArray,
            direction: this.direction,
            onDragStart: this.onDragStartFunc.bind(this),
            onDrag: this.onDragFunc.bind(this),
            onDragEnd: this.onDragEndFunc.bind(this),
            gutter: function() {
              var e = document.createElement("div");
              if ("bidirectional" === t.knob) {
                //KNOB
                var i = document.createElement("span");
                i.classList.add("knob"), i.style.backgroundColor = "var(--gray-01)", i.style.width = "var(--spacing-comp-05)", 
                i.style.height = "var(--spacing-comp-05)", i.style.display = "block", i.style.zIndex = "5", 
                i.style.position = "absolute", i.style.borderRadius = "50%", i.style.cursor = "pointer", 
                "vertical" === t.direction && (i.style.transform = "rotate(90deg)");
                //KNOB middle line
                var r = document.createElement("span");
                r.style.backgroundColor = "var(--gray-06)", r.style.width = "2px", r.style.height = "16px", 
                r.style.position = "relative", r.style.left = "11px", r.style.top = "4px", r.style.display = "block", 
                i.appendChild(r);
                //KNOB left side
                var s = document.createElement("span");
                s.style.width = "11px", s.style.height = "var(--spacing-comp-05)", s.style.display = "block", 
                s.style.top = "-16px", s.style.position = "relative", s.style.borderBottomLeftRadius = "40px", 
                s.style.borderTopLeftRadius = "40px", s.style.backgroundColor = "transparent", s.style.cursor = "pointer", 
                s.style.zIndex = "20", s.style.pointerEvents = "auto", s.addEventListener("click", t.knobLeftClicked.bind(t)), 
                s.onmouseover = t.knobLeftOver.bind(t), s.onmouseout = t.knobLeftOut.bind(t), i.appendChild(s);
                //KNOB right side
                var o = document.createElement("span");
                o.style.width = "11px", o.style.height = "var(--spacing-comp-05)", o.style.display = "block", 
                o.style.top = "-40px", o.style.right = "-13px", o.style.position = "relative", o.style.borderBottomRightRadius = "40px", 
                o.style.borderTopRightRadius = "40px", o.style.backgroundColor = "transparent", 
                o.style.cursor = "pointer", o.style.zIndex = "20", o.style.pointerEvents = "auto", 
                o.addEventListener("click", t.knobRightClicked.bind(t)), o.onmouseover = t.knobRightOver.bind(t), 
                o.onmouseout = t.knobRightOut.bind(t), i.appendChild(o), e.appendChild(i);
              }
              return e.className = "gutter gutter-" + t.direction, e;
            }
          });
          //set classes
          var e = this.el.querySelectorAll("gxg-split");
          "horizontal" === this.direction && e.forEach((function(t) {
            t.classList.add("split-horizontal");
          }));
        }, class_2.prototype.convertStringPropertiesToArray = function() {
          this.minSizeArray = this.minSize.split(",").map((function(t) {
            return parseInt(t, 10);
          })), this.sizesArray = this.sizes.split(",").map((function(t) {
            return parseInt(t, 10);
          }));
        }, class_2.prototype.getIds = function() {
          this.el.querySelectorAll("gxg-split").forEach(function(t) {
            var e = t.getAttribute("id");
            this.idsArray.push("#" + e);
          }.bind(this));
        }, class_2.prototype.validateSizes = function() {
          var t = 0, e = 0, i = this.sizesArray.length;
          if (this.sizesArray.forEach((function(e) {
            t += e;
          })), t > 100) {
            e = 100 / i, this.sizesArray = [];
            for (var r = 0; r < i; r++) this.sizesArray.push(e);
          }
        }, 
        //KNOB
        class_2.prototype.knobLeftClicked = function() {
          //add class to make the transition smooth
          var t = this.el.querySelectorAll("gxg-split");
          if (t.forEach((function(t) {
            t.classList.add("smooth-transition");
          })), this.forceCollapseZero) if (this.collapsedToZero) console.log("collapsed to zero"), 
          this.split.collapse(1), this.rightSplitReachedMin = !0, this.collapsedToZero = !1; else if (this.leftSplitReachedMin) {
            console.log("left Split Reached Min");
            var e = t[0], i = t[1];
            "horizontal" === this.direction ? (e.style.width = "0", i.style.width = "calc(100% - 8px)", 
            this.collapsedToZero = !0) : "vertical" === this.direction && (e.style.height = "0", 
            i.style.height = "calc(100% - 8px)", this.collapsedToZero = !0);
          } else this.split.collapse(0), this.leftSplitReachedMin = !0, this.rightSplitReachedMin = !1; else this.split.collapse(0), 
          setTimeout(function() {
            this.detectDragEndReachedMinimum();
          }.bind(this), 350);
        }, class_2.prototype.knobRightClicked = function() {
          //add class to make the transition smooth
          var t = this.el.querySelectorAll("gxg-split");
          if (t.forEach((function(t) {
            t.classList.add("smooth-transition");
          })), this.forceCollapseZero) if (this.collapsedToZero) console.log("collapsed to zero"), 
          this.split.collapse(0), this.rightSplitReachedMin = !1, this.collapsedToZero = !1; else if (this.rightSplitReachedMin) {
            console.log("right Split Reached Min");
            var e = t[0], i = t[1];
            "horizontal" === this.direction ? (e.style.width = "calc(100% - 8px)", i.style.width = "0", 
            this.collapsedToZero = !0) : "vertical" === this.direction && (e.style.height = "calc(100% - 8px)", 
            i.style.height = "0", this.collapsedToZero = !0);
          } else console.log("collapse 1"), this.split.collapse(1), this.rightSplitReachedMin = !0, 
          this.leftSplitReachedMin = !1; else this.split.collapse(1), setTimeout(function() {
            this.detectDragEndReachedMinimum();
          }.bind(this), 350);
        }, class_2.prototype.knobLeftOver = function() {
          this.el.classList.add("knob-left-hover");
        }, class_2.prototype.knobLeftOut = function() {
          this.el.classList.remove("knob-left-hover");
        }, class_2.prototype.knobRightOver = function() {
          this.el.classList.add("knob-right-hover");
        }, class_2.prototype.knobRightOut = function() {
          this.el.classList.remove("knob-right-hover");
        }, 
        //DRAG FUNCS
        class_2.prototype.onDragStartFunc = function() {
          this.el.querySelectorAll("gxg-split").forEach((function(t) {
            t.classList.remove("smooth-transition");
          }));
        }, class_2.prototype.onDragFunc = function() {
          this.detectDragEndReachedMinimum(), 
          //emmit dragging event
          this.dragging.emit("dragging");
        }, class_2.prototype.onDragEndFunc = function() {
          setTimeout(function() {
            //Emmit drag ended event
            this.dragEnded.emit("drag ended");
          }.bind(this), 350);
        }, class_2.prototype.detectDragEndReachedMinimum = function() {
          var t;
          "horizontal" === this.direction ? t = this.el.clientWidth : "vertical" === this.direction && (t = this.el.clientHeight);
          //actual sizes in percentages
                    var e = this.split.getSizes()[1], i = this.split.getSizes()[0], r = Math.trunc(e * t / 100), s = Math.trunc(i * t / 100);
          r < this.minSizeArray[1] + 15 && r > this.minSizeArray[1] - 15 || s < this.minSizeArray[0] + 15 && s > this.minSizeArray[0] - 15 ? (this.el.classList.add("gutter-reached-end"), 
          setTimeout(function() {
            this.el.classList.remove("gutter-reached-end");
          }.bind(this), 350)) : (this.el.classList.remove("gutter-reached-left"), this.el.classList.remove("gutter-reached-right"), 
          this.rightSplitReachedMin = !1, this.leftSplitReachedMin = !1), 
          //if guttter reached right:
          r < this.minSizeArray[1] + 15 && r > this.minSizeArray[1] - 15 && (this.forceCollapseZero ? (this.rightSplitReachedMin = !0, 
          this.leftSplitReachedMin = !1) : (this.el.classList.add("gutter-reached-right"), 
          this.el.classList.remove("gutter-reached-left"))), 
          //if guttter reached left:
          s < this.minSizeArray[0] + 15 && s > this.minSizeArray[0] - 15 && (this.forceCollapseZero ? (this.leftSplitReachedMin = !0, 
          this.rightSplitReachedMin = !1) : (this.el.classList.add("gutter-reached-left"), 
          this.el.classList.remove("gutter-reached-right")));
        }, 
        /**
                 * This method allows to collapse the split passsed as argument
                 */
        class_2.prototype.collapse = function(t, e) {
          return void 0 === e && (e = !1), __awaiter(this, void 0, void 0, (function() {
            var i, r, s;
            return __generator(this, (function(o) {
              return (i = this.el.querySelectorAll("gxg-split")).forEach((function(t) {
                t.classList.add("smooth-transition");
              })), r = i[0], s = i[1], this.forceCollapseZero && e ? ("horizontal" === this.direction ? 0 === t ? (r.style.width = "0", 
              s.style.width = "calc(100% - 8px)") : 1 === t && (r.style.width = "calc(100% - 8px)", 
              s.style.width = "0") : "vertical" === this.direction && (0 === t ? (r.style.height = "0", 
              s.style.height = "calc(100% - 8px)") : 1 === t && (r.style.height = "calc(100% - 8px)", 
              s.style.height = "0")), this.collapsedToZero = !0) : this.split.collapse(t), [ 2 /*return*/ ];
            }));
          }));
        }, class_2.prototype.render = function() {
          return i(r, {
            class: {
              "collapsed-to-zero": this.collapsedToZero
            }
          }, i("slot", null));
        }, Object.defineProperty(class_2.prototype, "el", {
          get: function() {
            return o(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_2;
      }()).style = ':host{display:block;width:100%;height:100%;overflow:hidden}:host ::slotted(.gutter-horizontal),:host ::slotted(.gutter-vertical){-webkit-transition:background-color 0.25s;transition:background-color 0.25s;background-color:var(--gray-01)}:host ::slotted(.gutter-horizontal){height:100%;float:left;cursor:col-resize;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;position:relative}:host ::slotted(.gutter-horizontal):after{right:-5px}:host ::slotted(.gutter-horizontal):before{left:-5px}:host ::slotted(.gutter-vertical){cursor:row-resize;display:-ms-flexbox;display:flex;-ms-flex-pack:center;justify-content:center;-ms-flex-align:center;align-items:center;background-color:var(--gray-01);position:relative}:host ::slotted(.gutter-vertical):after{right:50%;-webkit-transform:rotate(90deg) translateX(6px) translateY(-2px);transform:rotate(90deg) translateX(6px) translateY(-2px)}:host ::slotted(.gutter-vertical):before{left:50%;-webkit-transform:rotate(90deg) translateX(-6px) translateY(2px);transform:rotate(90deg) translateX(-6px) translateY(2px)}:host([knob=bidirectional]) ::slotted(.gutter-horizontal):after,:host([knob=bidirectional]) ::slotted(.gutter-vertical):after{content:"";width:0;height:0;border-top:4px solid transparent;border-bottom:4px solid transparent;border-left:4px solid var(--gray-06);z-index:10;position:absolute;pointer-events:none}:host([knob=bidirectional]) ::slotted(.gutter-horizontal):before,:host([knob=bidirectional]) ::slotted(.gutter-vertical):before{content:"";width:0;height:0;border-top:4px solid transparent;border-bottom:4px solid transparent;border-right:4px solid var(--gray-06);z-index:10;position:absolute;pointer-events:none}:host(.knob-left-hover) ::slotted(.gutter-horizontal):before,:host(.knob-left-hover) ::slotted(.gutter-vertical):before{border-top:4px solid transparent;border-bottom:4px solid transparent;border-right:4px solid var(--color-primary-active)}:host(.knob-right-hover) ::slotted(.gutter-horizontal):after,:host(.knob-right-hover) ::slotted(.gutter-vertical):after{border-top:4px solid transparent;border-bottom:4px solid transparent;border-left:4px solid var(--color-primary-active)}:host(.gutter-reached-end) ::slotted(.gutter-horizontal),:host(.gutter-reached-end) ::slotted(.gutter-vertical){background-color:var(--color-primary-active)}:host(.collapsed-to-zero) ::slotted(.gutter){pointer-events:none}:host(.collapsed-to-zero) ::slotted(.knob){pointer-events:auto}:host(.gutter-reached-right[knob=bidirectional]) ::slotted(.gutter-horizontal):after,:host(.gutter-reached-right[knob=bidirectional]) ::slotted(.gutter-vertical):after{display:none}:host(.gutter-reached-left[knob=bidirectional]) ::slotted(.gutter-horizontal):before,:host(.gutter-reached-left[knob=bidirectional]) ::slotted(.gutter-vertical):before{display:none}';
    }
  };
}));