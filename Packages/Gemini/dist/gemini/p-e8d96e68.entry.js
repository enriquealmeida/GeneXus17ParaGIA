import { r as t, h as e, H as i, c as s, g as r } from "./p-eb9dc661.js";

const o = class {
  constructor(e) {
    t(this, e);
  }
  render() {
    return e(i, null, e("slot", null));
  }
};

o.style = ":host{display:block;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;overflow-y:hidden;overflow-x:hidden;background-color:var(--color-background)}:host(.split-horizontal){height:100%;float:left}:host(.smooth-transition){-webkit-transition:width 0.35s, height 0.35s;transition:width 0.35s, height 0.35s;-webkit-transition-timing-function:ease-in-out;transition-timing-function:ease-in-out}";

// The programming goals of Split.js are to deliver readable, understandable and
// maintainable code, while at the same time manually optimizing for tiny minified file size,
// browser compatibility without additional requirements
// and very few assumptions about the user's page layout.
var n = "undefined" != typeof window ? window : null, l = null === n, a = l ? void 0 : n.document, NOOP = function() {
  return !1;
}, h = l ? "calc" : [ "", "-webkit-", "-moz-", "-o-" ].filter((function(t) {
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
  var s = t[e];
  return void 0 !== s ? s : i;
}, getGutterSize = function(t, e, i, s) {
  if (e) {
    if ("end" === s) return 0;
    if ("center" === s) return t / 2;
  } else if (i) {
    if ("start" === s) return 0;
    if ("center" === s) return t / 2;
  }
  return t;
}, defaultGutterFn = function(t, e) {
  var i = a.createElement("div");
  return i.className = "gutter gutter-" + e, i;
}, defaultElementStyleFn = function(t, e, i) {
  var s = {};
  return isString(e) ? s[t] = e : s[t] = h + "(" + e + "% - " + i + "px)", s;
}, defaultGutterStyleFn = function(t, e) {
  var i;
  return (i = {})[t] = e + "px", i;
}, Split = function(t, e) {
  if (void 0 === e && (e = {}), l) return {};
  var i, s, r, o, h, c, d = t;
  // Allow HTMLCollection to be used as an argument when supported
  Array.from && (d = Array.from(d));
  // All DOM elements in the split should have a common parent. We can grab
  // the first elements parent and hope users read the docs because the
  // behavior will be whacky otherwise.
    var u = elementOrSelector(d[0]).parentNode, g = getComputedStyle ? getComputedStyle(u) : null, p = g ? g.flexDirection : null, f = getOption(e, "sizes") || d.map((function() {
    return 100 / d.length;
  })), m = getOption(e, "minSize", 100), v = Array.isArray(m) ? m : d.map((function() {
    return m;
  })), b = getOption(e, "maxSize", 1 / 0), y = Array.isArray(b) ? b : d.map((function() {
    return b;
  })), z = getOption(e, "expandToMin", !1), S = getOption(e, "gutterSize", 10), x = getOption(e, "gutterAlign", "center"), k = getOption(e, "snapOffset", 30), E = getOption(e, "dragInterval", 1), L = getOption(e, "direction", "horizontal"), w = getOption(e, "cursor", "horizontal" === L ? "col-resize" : "row-resize"), M = getOption(e, "gutter", defaultGutterFn), R = getOption(e, "elementStyle", defaultElementStyleFn), A = getOption(e, "gutterStyle", defaultGutterStyleFn);
  // 3. Define the dragging helper functions, and a few helpers to go with them.
  // Each helper is bound to a pair object that contains its metadata. This
  // also makes it easy to store references to listeners that that will be
  // added and removed.
  // Even though there are no other functions contained in them, aliasing
  // this to self saves 50 bytes or so since it's used so frequently.
  // The pair object saves metadata like dragging state, position and
  // event listener references.
  function setElementSize(t, e, s, r) {
    // Split.js allows setting sizes via numbers (ideally), or if you must,
    // by string, like '300px'. This is less than ideal, because it breaks
    // the fluid layout that `calc(% - px)` provides. You're on your own if you do that,
    // make sure you calculate the gutter size by hand.
    var o = R(i, e, s, r);
    Object.keys(o).forEach((function(e) {
      // eslint-disable-next-line no-param-reassign
      t.style[e] = o[e];
    }));
  }
  function getSizes() {
    return c.map((function(t) {
      return t.size;
    }));
  }
  // Supports touch events, but not multitouch, so only the first
  // finger `touches[0]` is counted.
    function getMousePosition(t) {
    return "touches" in t ? t.touches[0][s] : t[s];
  }
  // Actually adjust the size of elements `a` and `b` to `offset` while dragging.
  // calc is used to allow calc(percentage + gutterpx) on the whole split instance,
  // which allows the viewport to be resized without additional logic.
  // Element a's size is the same as offset. b's size is total size - a size.
  // Both sizes are calculated from the initial parent percentage,
  // then the gutter size is subtracted.
    function adjust(t) {
    var e = c[this.a], i = c[this.b], s = e.size + i.size;
    e.size = t / this.size * s, i.size = s - t / this.size * s, setElementSize(e.element, e.size, this._b, e.i), 
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
    var i, s = c[this.a], r = c[this.b];
    this.dragging && (
    // Get the offset of the event from the first side of the
    // pair `this.start`. Then offset by the initial position of the
    // mouse compared to the gutter size.
    i = getMousePosition(t) - this.start + (this._b - this.dragOffset), E > 1 && (i = Math.round(i / E) * E), 
    // If within snapOffset of min or max, set offset to min or max.
    // snapOffset buffers a.minSize and b.minSize, so logic is opposite for both.
    // Include the appropriate gutter sizes to prevent overflows.
    i <= s.minSize + k + this._b ? i = s.minSize + this._b : i >= this.size - (r.minSize + k + this._c) && (i = this.size - (r.minSize + this._c)), 
    i >= s.maxSize - k + this._b ? i = s.maxSize + this._b : i <= this.size - (r.maxSize - k + this._c) && (i = this.size - (r.maxSize + this._c)), 
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
    var t = c[this.a].element, e = c[this.b].element, s = t.getBoundingClientRect(), n = e.getBoundingClientRect();
    this.size = s[i] + n[i] + this._b + this._c, this.start = s[r], this.end = s[o];
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
      var i = t[h];
      return 0 === i ? null : i -= "horizontal" === L ? parseFloat(e.paddingLeft) + parseFloat(e.paddingRight) : parseFloat(e.paddingTop) + parseFloat(e.paddingBottom);
    }(u);
    if (null === e) return t;
    if (v.reduce((function(t, e) {
      return t + e;
    }), 0) > e) return t;
    // Keep track of the excess pixels, the amount of pixels over the desired percentage
    // Also keep track of the elements with pixels to spare, to decrease after if needed
        var i = 0, s = [], r = t.map((function(r, o) {
      // Convert requested percentages to pixel sizes
      var n = e * r / 100, l = getGutterSize(S, 0 === o, o === t.length - 1, x), a = v[o] + l;
      // If element is too smal, increase excess pixels by the difference
      // and mark that it has no pixels to spare
      return n < a ? (i += a - n, s.push(0), a) : (
      // Otherwise, mark the pixels it has to spare and return it's original size
      s.push(n - a), n);
    }));
    // If nothing was adjusted, return the original sizes
    return 0 === i ? t : r.map((function(t, r) {
      var o = t;
      // While there's still pixels to take, and there's enough pixels to spare,
      // take as many as possible up to the total excess pixels
            if (i > 0 && s[r] - i > 0) {
        var n = Math.min(i, s[r] - i);
        // Subtract the amount taken for the next iteration
                i -= n, o = t - n;
      }
      // Return the pixel size adjusted as a percentage
            return o / e * 100;
    }));
  }
  // stopDragging is very similar to startDragging in reverse.
    function stopDragging() {
    var t = c[this.a].element, i = c[this.b].element;
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
      var i = c[this.a].element, s = c[this.b].element;
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
      s.addEventListener("selectstart", NOOP), s.addEventListener("dragstart", NOOP), 
      i.style.userSelect = "none", i.style.webkitUserSelect = "none", i.style.MozUserSelect = "none", 
      i.style.pointerEvents = "none", s.style.userSelect = "none", s.style.webkitUserSelect = "none", 
      s.style.MozUserSelect = "none", s.style.pointerEvents = "none", 
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
  "horizontal" === L ? (i = "width", s = "clientX", r = "left", o = "right", h = "clientWidth") : "vertical" === L && (i = "height", 
  s = "clientY", r = "top", o = "bottom", h = "clientHeight"), f = trimToMin(f);
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
  var T = [];
  function adjustToMin(t) {
    var e = t.i === T.length, i = e ? T[t.i - 1] : T[t.i];
    calculateSizes.call(i);
    var s = e ? i.size - t.minSize - i._c : t.minSize + i._b;
    adjust.call(i, s);
  }
  return (c = d.map((function(t, e) {
    // Create the element object.
    var s, r = {
      element: elementOrSelector(t),
      size: f[e],
      minSize: v[e],
      maxSize: y[e],
      i: e
    };
    if (e > 0 && (
    // Create the pair object with its metadata.
    (s = {
      a: e - 1,
      b: e,
      dragging: !1,
      direction: L,
      parent: u
    })._b = getGutterSize(S, e - 1 == 0, !1, x), s._c = getGutterSize(S, !1, e === d.length - 1, x), 
    "row-reverse" === p || "column-reverse" === p)) {
      var o = s.a;
      s.a = s.b, s.b = o;
    }
    // Determine the size of the current element. IE8 is supported by
    // staticly assigning sizes without draggable gutters. Assigns a string
    // to `size`.
    
    // Create gutter elements for each pair.
        if (e > 0) {
      var n = M(e, L, r.element);
      !function(t, e, s) {
        var r = A(i, e, s);
        Object.keys(r).forEach((function(e) {
          // eslint-disable-next-line no-param-reassign
          t.style[e] = r[e];
        }));
      }(n, S, e), 
      // Save bound event listener for removal later
      s._a = startDragging.bind(s), 
      // Attach bound event listener
      n.addEventListener("mousedown", s._a), n.addEventListener("touchstart", s._a), u.insertBefore(n, r.element), 
      s.gutter = n;
    }
    return setElementSize(r.element, r.size, getGutterSize(S, 0 === e, e === d.length - 1, x), e), 
    // After the first iteration, and we have a pair object, append it to the
    // list of pairs.
    e > 0 && T.push(s), r;
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
          var s = T[i - 1], r = c[s.a], o = c[s.b];
          r.size = e[i - 1], o.size = t, setElementSize(r.element, r.size, s._b, r.i), setElementSize(o.element, o.size, s._c, o.i);
        }
      }));
    },
    getSizes,
    collapse: function(t) {
      adjustToMin(c[t]);
    },
    destroy: function(t, e) {
      T.forEach((function(s) {
        if (!0 !== e ? s.parent.removeChild(s.gutter) : (s.gutter.removeEventListener("mousedown", s._a), 
        s.gutter.removeEventListener("touchstart", s._a)), !0 !== t) {
          var r = R(i, s.a.size, s._b);
          Object.keys(r).forEach((function(t) {
            c[s.a].element.style[t] = "", c[s.b].element.style[t] = "";
          }));
        }
      }));
    },
    parent: u,
    pairs: T
  };
};

const c = class {
  constructor(e) {
    t(this, e), this.dragging = s(this, "dragging", 7), this.dragEnded = s(this, "dragEnded", 7), 
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
  makeId(t) {
    const e = [], i = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", s = i.length;
    for (let r = 0; r < t; r++) e.push(i.charAt(Math.floor(Math.random() * s)));
    // this prevents the id begining with a number, which is not allowed
    return e.unshift("a"), e.join("");
  }
  componentWillLoad() {
    this.el.querySelectorAll("gxg-split").forEach(function(t) {
      const e = this.makeId(5);
      t.setAttribute("id", e);
    }.bind(this));
  }
  componentDidLoad() {
    this.getIds(), this.convertStringPropertiesToArray(), this.validateSizes(), this.split = Split(this.idsArray, {
      gutterSize: 8,
      cursor: "col-resize",
      minSize: this.minSizeArray,
      sizes: this.sizesArray,
      direction: this.direction,
      onDragStart: this.onDragStartFunc.bind(this),
      onDrag: this.onDragFunc.bind(this),
      onDragEnd: this.onDragEndFunc.bind(this),
      gutter: () => {
        const t = document.createElement("div");
        if ("bidirectional" === this.knob) {
          //KNOB
          const e = document.createElement("span");
          e.classList.add("knob"), e.style.backgroundColor = "var(--gray-01)", e.style.width = "var(--spacing-comp-05)", 
          e.style.height = "var(--spacing-comp-05)", e.style.display = "block", e.style.zIndex = "5", 
          e.style.position = "absolute", e.style.borderRadius = "50%", e.style.cursor = "pointer", 
          "vertical" === this.direction && (e.style.transform = "rotate(90deg)");
          //KNOB middle line
          const i = document.createElement("span");
          i.style.backgroundColor = "var(--gray-06)", i.style.width = "2px", i.style.height = "16px", 
          i.style.position = "relative", i.style.left = "11px", i.style.top = "4px", i.style.display = "block", 
          e.appendChild(i);
          //KNOB left side
          const s = document.createElement("span");
          s.style.width = "11px", s.style.height = "var(--spacing-comp-05)", s.style.display = "block", 
          s.style.top = "-16px", s.style.position = "relative", s.style.borderBottomLeftRadius = "40px", 
          s.style.borderTopLeftRadius = "40px", s.style.backgroundColor = "transparent", s.style.cursor = "pointer", 
          s.style.zIndex = "20", s.style.pointerEvents = "auto", s.addEventListener("click", this.knobLeftClicked.bind(this)), 
          s.onmouseover = this.knobLeftOver.bind(this), s.onmouseout = this.knobLeftOut.bind(this), 
          e.appendChild(s);
          //KNOB right side
          const r = document.createElement("span");
          r.style.width = "11px", r.style.height = "var(--spacing-comp-05)", r.style.display = "block", 
          r.style.top = "-40px", r.style.right = "-13px", r.style.position = "relative", r.style.borderBottomRightRadius = "40px", 
          r.style.borderTopRightRadius = "40px", r.style.backgroundColor = "transparent", 
          r.style.cursor = "pointer", r.style.zIndex = "20", r.style.pointerEvents = "auto", 
          r.addEventListener("click", this.knobRightClicked.bind(this)), r.onmouseover = this.knobRightOver.bind(this), 
          r.onmouseout = this.knobRightOut.bind(this), e.appendChild(r), t.appendChild(e);
        }
        return t.className = "gutter gutter-" + this.direction, t;
      }
    });
    //set classes
    const t = this.el.querySelectorAll("gxg-split");
    "horizontal" === this.direction && t.forEach((function(t) {
      t.classList.add("split-horizontal");
    }));
  }
  convertStringPropertiesToArray() {
    this.minSizeArray = this.minSize.split(",").map((function(t) {
      return parseInt(t, 10);
    })), this.sizesArray = this.sizes.split(",").map((function(t) {
      return parseInt(t, 10);
    }));
  }
  getIds() {
    this.el.querySelectorAll("gxg-split").forEach(function(t) {
      const e = t.getAttribute("id");
      this.idsArray.push("#" + e);
    }.bind(this));
  }
  validateSizes() {
    let t = 0, e = 0;
    const i = this.sizesArray.length;
    if (this.sizesArray.forEach((function(e) {
      t += e;
    })), t > 100) {
      e = 100 / i, this.sizesArray = [];
      for (let t = 0; t < i; t++) this.sizesArray.push(e);
    }
  }
  //KNOB
  knobLeftClicked() {
    //add class to make the transition smooth
    const t = this.el.querySelectorAll("gxg-split");
    if (t.forEach((function(t) {
      t.classList.add("smooth-transition");
    })), this.forceCollapseZero) if (this.collapsedToZero) console.log("collapsed to zero"), 
    this.split.collapse(1), this.rightSplitReachedMin = !0, this.collapsedToZero = !1; else if (this.leftSplitReachedMin) {
      console.log("left Split Reached Min");
      const e = t[0], i = t[1];
      "horizontal" === this.direction ? (e.style.width = "0", i.style.width = "calc(100% - 8px)", 
      this.collapsedToZero = !0) : "vertical" === this.direction && (e.style.height = "0", 
      i.style.height = "calc(100% - 8px)", this.collapsedToZero = !0);
    } else this.split.collapse(0), this.leftSplitReachedMin = !0, this.rightSplitReachedMin = !1; else this.split.collapse(0), 
    setTimeout(function() {
      this.detectDragEndReachedMinimum();
    }.bind(this), 350);
  }
  knobRightClicked() {
    //add class to make the transition smooth
    const t = this.el.querySelectorAll("gxg-split");
    if (t.forEach((function(t) {
      t.classList.add("smooth-transition");
    })), this.forceCollapseZero) if (this.collapsedToZero) console.log("collapsed to zero"), 
    this.split.collapse(0), this.rightSplitReachedMin = !1, this.collapsedToZero = !1; else if (this.rightSplitReachedMin) {
      console.log("right Split Reached Min");
      const e = t[0], i = t[1];
      "horizontal" === this.direction ? (e.style.width = "calc(100% - 8px)", i.style.width = "0", 
      this.collapsedToZero = !0) : "vertical" === this.direction && (e.style.height = "calc(100% - 8px)", 
      i.style.height = "0", this.collapsedToZero = !0);
    } else console.log("collapse 1"), this.split.collapse(1), this.rightSplitReachedMin = !0, 
    this.leftSplitReachedMin = !1; else this.split.collapse(1), setTimeout(function() {
      this.detectDragEndReachedMinimum();
    }.bind(this), 350);
  }
  knobLeftOver() {
    this.el.classList.add("knob-left-hover");
  }
  knobLeftOut() {
    this.el.classList.remove("knob-left-hover");
  }
  knobRightOver() {
    this.el.classList.add("knob-right-hover");
  }
  knobRightOut() {
    this.el.classList.remove("knob-right-hover");
  }
  //DRAG FUNCS
  onDragStartFunc() {
    this.el.querySelectorAll("gxg-split").forEach((function(t) {
      t.classList.remove("smooth-transition");
    }));
  }
  onDragFunc() {
    this.detectDragEndReachedMinimum(), 
    //emmit dragging event
    this.dragging.emit("dragging");
  }
  onDragEndFunc() {
    setTimeout(function() {
      //Emmit drag ended event
      this.dragEnded.emit("drag ended");
    }.bind(this), 350);
  }
  detectDragEndReachedMinimum() {
    let t;
    "horizontal" === this.direction ? t = this.el.clientWidth : "vertical" === this.direction && (t = this.el.clientHeight);
    //actual sizes in percentages
        const e = this.split.getSizes()[1], i = this.split.getSizes()[0], s = Math.trunc(e * t / 100), r = Math.trunc(i * t / 100);
    s < this.minSizeArray[1] + 15 && s > this.minSizeArray[1] - 15 || r < this.minSizeArray[0] + 15 && r > this.minSizeArray[0] - 15 ? (this.el.classList.add("gutter-reached-end"), 
    setTimeout(function() {
      this.el.classList.remove("gutter-reached-end");
    }.bind(this), 350)) : (this.el.classList.remove("gutter-reached-left"), this.el.classList.remove("gutter-reached-right"), 
    this.rightSplitReachedMin = !1, this.leftSplitReachedMin = !1), 
    //if guttter reached right:
    s < this.minSizeArray[1] + 15 && s > this.minSizeArray[1] - 15 && (this.forceCollapseZero ? (this.rightSplitReachedMin = !0, 
    this.leftSplitReachedMin = !1) : (this.el.classList.add("gutter-reached-right"), 
    this.el.classList.remove("gutter-reached-left"))), 
    //if guttter reached left:
    r < this.minSizeArray[0] + 15 && r > this.minSizeArray[0] - 15 && (this.forceCollapseZero ? (this.leftSplitReachedMin = !0, 
    this.rightSplitReachedMin = !1) : (this.el.classList.add("gutter-reached-left"), 
    this.el.classList.remove("gutter-reached-right")));
  }
  /**
     * This method allows to collapse the split passsed as argument
     */  async collapse(t, e = !1) {
    //add class to make the transition smooth
    const i = this.el.querySelectorAll("gxg-split");
    i.forEach((function(t) {
      t.classList.add("smooth-transition");
    }));
    const s = i[0], r = i[1];
    this.forceCollapseZero && e ? ("horizontal" === this.direction ? 0 === t ? (s.style.width = "0", 
    r.style.width = "calc(100% - 8px)") : 1 === t && (s.style.width = "calc(100% - 8px)", 
    r.style.width = "0") : "vertical" === this.direction && (0 === t ? (s.style.height = "0", 
    r.style.height = "calc(100% - 8px)") : 1 === t && (s.style.height = "calc(100% - 8px)", 
    r.style.height = "0")), this.collapsedToZero = !0) : this.split.collapse(t);
  }
  render() {
    return e(i, {
      class: {
        "collapsed-to-zero": this.collapsedToZero
      }
    }, e("slot", null));
  }
  get el() {
    return r(this);
  }
};

c.style = ':host{display:block;width:100%;height:100%;overflow:hidden}:host ::slotted(.gutter-horizontal),:host ::slotted(.gutter-vertical){-webkit-transition:background-color 0.25s;transition:background-color 0.25s;background-color:var(--gray-01)}:host ::slotted(.gutter-horizontal){height:100%;float:left;cursor:col-resize;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;position:relative}:host ::slotted(.gutter-horizontal):after{right:-5px}:host ::slotted(.gutter-horizontal):before{left:-5px}:host ::slotted(.gutter-vertical){cursor:row-resize;display:-ms-flexbox;display:flex;-ms-flex-pack:center;justify-content:center;-ms-flex-align:center;align-items:center;background-color:var(--gray-01);position:relative}:host ::slotted(.gutter-vertical):after{right:50%;-webkit-transform:rotate(90deg) translateX(6px) translateY(-2px);transform:rotate(90deg) translateX(6px) translateY(-2px)}:host ::slotted(.gutter-vertical):before{left:50%;-webkit-transform:rotate(90deg) translateX(-6px) translateY(2px);transform:rotate(90deg) translateX(-6px) translateY(2px)}:host([knob=bidirectional]) ::slotted(.gutter-horizontal):after,:host([knob=bidirectional]) ::slotted(.gutter-vertical):after{content:"";width:0;height:0;border-top:4px solid transparent;border-bottom:4px solid transparent;border-left:4px solid var(--gray-06);z-index:10;position:absolute;pointer-events:none}:host([knob=bidirectional]) ::slotted(.gutter-horizontal):before,:host([knob=bidirectional]) ::slotted(.gutter-vertical):before{content:"";width:0;height:0;border-top:4px solid transparent;border-bottom:4px solid transparent;border-right:4px solid var(--gray-06);z-index:10;position:absolute;pointer-events:none}:host(.knob-left-hover) ::slotted(.gutter-horizontal):before,:host(.knob-left-hover) ::slotted(.gutter-vertical):before{border-top:4px solid transparent;border-bottom:4px solid transparent;border-right:4px solid var(--color-primary-active)}:host(.knob-right-hover) ::slotted(.gutter-horizontal):after,:host(.knob-right-hover) ::slotted(.gutter-vertical):after{border-top:4px solid transparent;border-bottom:4px solid transparent;border-left:4px solid var(--color-primary-active)}:host(.gutter-reached-end) ::slotted(.gutter-horizontal),:host(.gutter-reached-end) ::slotted(.gutter-vertical){background-color:var(--color-primary-active)}:host(.collapsed-to-zero) ::slotted(.gutter){pointer-events:none}:host(.collapsed-to-zero) ::slotted(.knob){pointer-events:auto}:host(.gutter-reached-right[knob=bidirectional]) ::slotted(.gutter-horizontal):after,:host(.gutter-reached-right[knob=bidirectional]) ::slotted(.gutter-vertical):after{display:none}:host(.gutter-reached-left[knob=bidirectional]) ::slotted(.gutter-horizontal):before,:host(.gutter-reached-left[knob=bidirectional]) ::slotted(.gutter-vertical):before{display:none}';

export { o as gxg_split, c as gxg_splitter }