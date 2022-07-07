'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const splitCss = ":host{display:block;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;overflow-y:hidden;overflow-x:hidden;background-color:var(--color-background)}:host(.split-horizontal){height:100%;float:left}:host(.smooth-transition){-webkit-transition:width 0.35s, height 0.35s;transition:width 0.35s, height 0.35s;-webkit-transition-timing-function:ease-in-out;transition-timing-function:ease-in-out}";

const GxgSplit = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
    }
    render() {
        return (index.h(index.Host, null, index.h("slot", null)));
    }
};
GxgSplit.style = splitCss;

// The programming goals of Split.js are to deliver readable, understandable and
// maintainable code, while at the same time manually optimizing for tiny minified file size,
// browser compatibility without additional requirements
// and very few assumptions about the user's page layout.
var global = typeof window !== 'undefined' ? window : null;
var ssr = global === null;
var document$1 = !ssr ? global.document : undefined;

// Save a couple long function names that are used frequently.
// This optimization saves around 400 bytes.
var addEventListener = 'addEventListener';
var removeEventListener = 'removeEventListener';
var getBoundingClientRect = 'getBoundingClientRect';
var gutterStartDragging = '_a';
var aGutterSize = '_b';
var bGutterSize = '_c';
var HORIZONTAL = 'horizontal';
var NOOP = function () { return false; };

// Helper function determines which prefixes of CSS calc we need.
// We only need to do this once on startup, when this anonymous function is called.
//
// Tests -webkit, -moz and -o prefixes. Modified from StackOverflow:
// http://stackoverflow.com/questions/16625140/js-feature-detection-to-detect-the-usage-of-webkit-calc-over-calc/16625167#16625167
var calc = ssr
    ? 'calc'
    : ((['', '-webkit-', '-moz-', '-o-']
          .filter(function (prefix) {
              var el = document$1.createElement('div');
              el.style.cssText = "width:" + prefix + "calc(9px)";

              return !!el.style.length
          })
          .shift()) + "calc");

// Helper function checks if its argument is a string-like type
var isString = function (v) { return typeof v === 'string' || v instanceof String; };

// Helper function allows elements and string selectors to be used
// interchangeably. In either case an element is returned. This allows us to
// do `Split([elem1, elem2])` as well as `Split(['#id1', '#id2'])`.
var elementOrSelector = function (el) {
    if (isString(el)) {
        var ele = document$1.querySelector(el);
        if (!ele) {
            throw new Error(("Selector " + el + " did not match a DOM element"))
        }
        return ele
    }

    return el
};

// Helper function gets a property from the properties object, with a default fallback
var getOption = function (options, propName, def) {
    var value = options[propName];
    if (value !== undefined) {
        return value
    }
    return def
};

var getGutterSize = function (gutterSize, isFirst, isLast, gutterAlign) {
    if (isFirst) {
        if (gutterAlign === 'end') {
            return 0
        }
        if (gutterAlign === 'center') {
            return gutterSize / 2
        }
    } else if (isLast) {
        if (gutterAlign === 'start') {
            return 0
        }
        if (gutterAlign === 'center') {
            return gutterSize / 2
        }
    }

    return gutterSize
};

// Default options
var defaultGutterFn = function (i, gutterDirection) {
    var gut = document$1.createElement('div');
    gut.className = "gutter gutter-" + gutterDirection;
    return gut
};

var defaultElementStyleFn = function (dim, size, gutSize) {
    var style = {};

    if (!isString(size)) {
        style[dim] = calc + "(" + size + "% - " + gutSize + "px)";
    } else {
        style[dim] = size;
    }

    return style
};

var defaultGutterStyleFn = function (dim, gutSize) {
    var obj;

    return (( obj = {}, obj[dim] = (gutSize + "px"), obj ));
};

// The main function to initialize a split. Split.js thinks about each pair
// of elements as an independant pair. Dragging the gutter between two elements
// only changes the dimensions of elements in that pair. This is key to understanding
// how the following functions operate, since each function is bound to a pair.
//
// A pair object is shaped like this:
//
// {
//     a: DOM element,
//     b: DOM element,
//     aMin: Number,
//     bMin: Number,
//     dragging: Boolean,
//     parent: DOM element,
//     direction: 'horizontal' | 'vertical'
// }
//
// The basic sequence:
//
// 1. Set defaults to something sane. `options` doesn't have to be passed at all.
// 2. Initialize a bunch of strings based on the direction we're splitting.
//    A lot of the behavior in the rest of the library is paramatized down to
//    rely on CSS strings and classes.
// 3. Define the dragging helper functions, and a few helpers to go with them.
// 4. Loop through the elements while pairing them off. Every pair gets an
//    `pair` object and a gutter.
// 5. Actually size the pair elements, insert gutters and attach event listeners.
var Split = function (idsOption, options) {
    if ( options === void 0 ) options = {};

    if (ssr) { return {} }

    var ids = idsOption;
    var dimension;
    var clientAxis;
    var position;
    var positionEnd;
    var clientSize;
    var elements;

    // Allow HTMLCollection to be used as an argument when supported
    if (Array.from) {
        ids = Array.from(ids);
    }

    // All DOM elements in the split should have a common parent. We can grab
    // the first elements parent and hope users read the docs because the
    // behavior will be whacky otherwise.
    var firstElement = elementOrSelector(ids[0]);
    var parent = firstElement.parentNode;
    var parentStyle = getComputedStyle ? getComputedStyle(parent) : null;
    var parentFlexDirection = parentStyle ? parentStyle.flexDirection : null;

    // Set default options.sizes to equal percentages of the parent element.
    var sizes = getOption(options, 'sizes') || ids.map(function () { return 100 / ids.length; });

    // Standardize minSize and maxSize to an array if it isn't already.
    // This allows minSize and maxSize to be passed as a number.
    var minSize = getOption(options, 'minSize', 100);
    var minSizes = Array.isArray(minSize) ? minSize : ids.map(function () { return minSize; });
    var maxSize = getOption(options, 'maxSize', Infinity);
    var maxSizes = Array.isArray(maxSize) ? maxSize : ids.map(function () { return maxSize; });

    // Get other options
    var expandToMin = getOption(options, 'expandToMin', false);
    var gutterSize = getOption(options, 'gutterSize', 10);
    var gutterAlign = getOption(options, 'gutterAlign', 'center');
    var snapOffset = getOption(options, 'snapOffset', 30);
    var dragInterval = getOption(options, 'dragInterval', 1);
    var direction = getOption(options, 'direction', HORIZONTAL);
    var cursor = getOption(
        options,
        'cursor',
        direction === HORIZONTAL ? 'col-resize' : 'row-resize'
    );
    var gutter = getOption(options, 'gutter', defaultGutterFn);
    var elementStyle = getOption(
        options,
        'elementStyle',
        defaultElementStyleFn
    );
    var gutterStyle = getOption(options, 'gutterStyle', defaultGutterStyleFn);

    // 2. Initialize a bunch of strings based on the direction we're splitting.
    // A lot of the behavior in the rest of the library is paramatized down to
    // rely on CSS strings and classes.
    if (direction === HORIZONTAL) {
        dimension = 'width';
        clientAxis = 'clientX';
        position = 'left';
        positionEnd = 'right';
        clientSize = 'clientWidth';
    } else if (direction === 'vertical') {
        dimension = 'height';
        clientAxis = 'clientY';
        position = 'top';
        positionEnd = 'bottom';
        clientSize = 'clientHeight';
    }

    // 3. Define the dragging helper functions, and a few helpers to go with them.
    // Each helper is bound to a pair object that contains its metadata. This
    // also makes it easy to store references to listeners that that will be
    // added and removed.
    //
    // Even though there are no other functions contained in them, aliasing
    // this to self saves 50 bytes or so since it's used so frequently.
    //
    // The pair object saves metadata like dragging state, position and
    // event listener references.

    function setElementSize(el, size, gutSize, i) {
        // Split.js allows setting sizes via numbers (ideally), or if you must,
        // by string, like '300px'. This is less than ideal, because it breaks
        // the fluid layout that `calc(% - px)` provides. You're on your own if you do that,
        // make sure you calculate the gutter size by hand.
        var style = elementStyle(dimension, size, gutSize, i);

        Object.keys(style).forEach(function (prop) {
            // eslint-disable-next-line no-param-reassign
            el.style[prop] = style[prop];
        });
    }

    function setGutterSize(gutterElement, gutSize, i) {
        var style = gutterStyle(dimension, gutSize, i);

        Object.keys(style).forEach(function (prop) {
            // eslint-disable-next-line no-param-reassign
            gutterElement.style[prop] = style[prop];
        });
    }

    function getSizes() {
        return elements.map(function (element) { return element.size; })
    }

    // Supports touch events, but not multitouch, so only the first
    // finger `touches[0]` is counted.
    function getMousePosition(e) {
        if ('touches' in e) { return e.touches[0][clientAxis] }
        return e[clientAxis]
    }

    // Actually adjust the size of elements `a` and `b` to `offset` while dragging.
    // calc is used to allow calc(percentage + gutterpx) on the whole split instance,
    // which allows the viewport to be resized without additional logic.
    // Element a's size is the same as offset. b's size is total size - a size.
    // Both sizes are calculated from the initial parent percentage,
    // then the gutter size is subtracted.
    function adjust(offset) {
        var a = elements[this.a];
        var b = elements[this.b];
        var percentage = a.size + b.size;

        a.size = (offset / this.size) * percentage;
        b.size = percentage - (offset / this.size) * percentage;

        setElementSize(a.element, a.size, this[aGutterSize], a.i);
        setElementSize(b.element, b.size, this[bGutterSize], b.i);
    }

    // drag, where all the magic happens. The logic is really quite simple:
    //
    // 1. Ignore if the pair is not dragging.
    // 2. Get the offset of the event.
    // 3. Snap offset to min if within snappable range (within min + snapOffset).
    // 4. Actually adjust each element in the pair to offset.
    //
    // ---------------------------------------------------------------------
    // |    | <- a.minSize               ||              b.minSize -> |    |
    // |    |  | <- this.snapOffset      ||     this.snapOffset -> |  |    |
    // |    |  |                         ||                        |  |    |
    // |    |  |                         ||                        |  |    |
    // ---------------------------------------------------------------------
    // | <- this.start                                        this.size -> |
    function drag(e) {
        var offset;
        var a = elements[this.a];
        var b = elements[this.b];

        if (!this.dragging) { return }

        // Get the offset of the event from the first side of the
        // pair `this.start`. Then offset by the initial position of the
        // mouse compared to the gutter size.
        offset =
            getMousePosition(e) -
            this.start +
            (this[aGutterSize] - this.dragOffset);

        if (dragInterval > 1) {
            offset = Math.round(offset / dragInterval) * dragInterval;
        }

        // If within snapOffset of min or max, set offset to min or max.
        // snapOffset buffers a.minSize and b.minSize, so logic is opposite for both.
        // Include the appropriate gutter sizes to prevent overflows.
        if (offset <= a.minSize + snapOffset + this[aGutterSize]) {
            offset = a.minSize + this[aGutterSize];
        } else if (
            offset >=
            this.size - (b.minSize + snapOffset + this[bGutterSize])
        ) {
            offset = this.size - (b.minSize + this[bGutterSize]);
        }

        if (offset >= a.maxSize - snapOffset + this[aGutterSize]) {
            offset = a.maxSize + this[aGutterSize];
        } else if (
            offset <=
            this.size - (b.maxSize - snapOffset + this[bGutterSize])
        ) {
            offset = this.size - (b.maxSize + this[bGutterSize]);
        }

        // Actually adjust the size.
        adjust.call(this, offset);

        // Call the drag callback continously. Don't do anything too intensive
        // in this callback.
        getOption(options, 'onDrag', NOOP)(getSizes());
    }

    // Cache some important sizes when drag starts, so we don't have to do that
    // continously:
    //
    // `size`: The total size of the pair. First + second + first gutter + second gutter.
    // `start`: The leading side of the first element.
    //
    // ------------------------------------------------
    // |      aGutterSize -> |||                      |
    // |                     |||                      |
    // |                     |||                      |
    // |                     ||| <- bGutterSize       |
    // ------------------------------------------------
    // | <- start                             size -> |
    function calculateSizes() {
        // Figure out the parent size minus padding.
        var a = elements[this.a].element;
        var b = elements[this.b].element;

        var aBounds = a[getBoundingClientRect]();
        var bBounds = b[getBoundingClientRect]();

        this.size =
            aBounds[dimension] +
            bBounds[dimension] +
            this[aGutterSize] +
            this[bGutterSize];
        this.start = aBounds[position];
        this.end = aBounds[positionEnd];
    }

    function innerSize(element) {
        // Return nothing if getComputedStyle is not supported (< IE9)
        // Or if parent element has no layout yet
        if (!getComputedStyle) { return null }

        var computedStyle = getComputedStyle(element);

        if (!computedStyle) { return null }

        var size = element[clientSize];

        if (size === 0) { return null }

        if (direction === HORIZONTAL) {
            size -=
                parseFloat(computedStyle.paddingLeft) +
                parseFloat(computedStyle.paddingRight);
        } else {
            size -=
                parseFloat(computedStyle.paddingTop) +
                parseFloat(computedStyle.paddingBottom);
        }

        return size
    }

    // When specifying percentage sizes that are less than the computed
    // size of the element minus the gutter, the lesser percentages must be increased
    // (and decreased from the other elements) to make space for the pixels
    // subtracted by the gutters.
    function trimToMin(sizesToTrim) {
        // Try to get inner size of parent element.
        // If it's no supported, return original sizes.
        var parentSize = innerSize(parent);
        if (parentSize === null) {
            return sizesToTrim
        }

        if (minSizes.reduce(function (a, b) { return a + b; }, 0) > parentSize) {
            return sizesToTrim
        }

        // Keep track of the excess pixels, the amount of pixels over the desired percentage
        // Also keep track of the elements with pixels to spare, to decrease after if needed
        var excessPixels = 0;
        var toSpare = [];

        var pixelSizes = sizesToTrim.map(function (size, i) {
            // Convert requested percentages to pixel sizes
            var pixelSize = (parentSize * size) / 100;
            var elementGutterSize = getGutterSize(
                gutterSize,
                i === 0,
                i === sizesToTrim.length - 1,
                gutterAlign
            );
            var elementMinSize = minSizes[i] + elementGutterSize;

            // If element is too smal, increase excess pixels by the difference
            // and mark that it has no pixels to spare
            if (pixelSize < elementMinSize) {
                excessPixels += elementMinSize - pixelSize;
                toSpare.push(0);
                return elementMinSize
            }

            // Otherwise, mark the pixels it has to spare and return it's original size
            toSpare.push(pixelSize - elementMinSize);
            return pixelSize
        });

        // If nothing was adjusted, return the original sizes
        if (excessPixels === 0) {
            return sizesToTrim
        }

        return pixelSizes.map(function (pixelSize, i) {
            var newPixelSize = pixelSize;

            // While there's still pixels to take, and there's enough pixels to spare,
            // take as many as possible up to the total excess pixels
            if (excessPixels > 0 && toSpare[i] - excessPixels > 0) {
                var takenPixels = Math.min(
                    excessPixels,
                    toSpare[i] - excessPixels
                );

                // Subtract the amount taken for the next iteration
                excessPixels -= takenPixels;
                newPixelSize = pixelSize - takenPixels;
            }

            // Return the pixel size adjusted as a percentage
            return (newPixelSize / parentSize) * 100
        })
    }

    // stopDragging is very similar to startDragging in reverse.
    function stopDragging() {
        var self = this;
        var a = elements[self.a].element;
        var b = elements[self.b].element;

        if (self.dragging) {
            getOption(options, 'onDragEnd', NOOP)(getSizes());
        }

        self.dragging = false;

        // Remove the stored event listeners. This is why we store them.
        global[removeEventListener]('mouseup', self.stop);
        global[removeEventListener]('touchend', self.stop);
        global[removeEventListener]('touchcancel', self.stop);
        global[removeEventListener]('mousemove', self.move);
        global[removeEventListener]('touchmove', self.move);

        // Clear bound function references
        self.stop = null;
        self.move = null;

        a[removeEventListener]('selectstart', NOOP);
        a[removeEventListener]('dragstart', NOOP);
        b[removeEventListener]('selectstart', NOOP);
        b[removeEventListener]('dragstart', NOOP);

        a.style.userSelect = '';
        a.style.webkitUserSelect = '';
        a.style.MozUserSelect = '';
        a.style.pointerEvents = '';

        b.style.userSelect = '';
        b.style.webkitUserSelect = '';
        b.style.MozUserSelect = '';
        b.style.pointerEvents = '';

        self.gutter.style.cursor = '';
        self.parent.style.cursor = '';
        document$1.body.style.cursor = '';
    }

    // startDragging calls `calculateSizes` to store the inital size in the pair object.
    // It also adds event listeners for mouse/touch events,
    // and prevents selection while dragging so avoid the selecting text.
    function startDragging(e) {
        // Right-clicking can't start dragging.
        if ('button' in e && e.button !== 0) {
            return
        }

        // Alias frequently used variables to save space. 200 bytes.
        var self = this;
        var a = elements[self.a].element;
        var b = elements[self.b].element;

        // Call the onDragStart callback.
        if (!self.dragging) {
            getOption(options, 'onDragStart', NOOP)(getSizes());
        }

        // Don't actually drag the element. We emulate that in the drag function.
        e.preventDefault();

        // Set the dragging property of the pair object.
        self.dragging = true;

        // Create two event listeners bound to the same pair object and store
        // them in the pair object.
        self.move = drag.bind(self);
        self.stop = stopDragging.bind(self);

        // All the binding. `window` gets the stop events in case we drag out of the elements.
        global[addEventListener]('mouseup', self.stop);
        global[addEventListener]('touchend', self.stop);
        global[addEventListener]('touchcancel', self.stop);
        global[addEventListener]('mousemove', self.move);
        global[addEventListener]('touchmove', self.move);

        // Disable selection. Disable!
        a[addEventListener]('selectstart', NOOP);
        a[addEventListener]('dragstart', NOOP);
        b[addEventListener]('selectstart', NOOP);
        b[addEventListener]('dragstart', NOOP);

        a.style.userSelect = 'none';
        a.style.webkitUserSelect = 'none';
        a.style.MozUserSelect = 'none';
        a.style.pointerEvents = 'none';

        b.style.userSelect = 'none';
        b.style.webkitUserSelect = 'none';
        b.style.MozUserSelect = 'none';
        b.style.pointerEvents = 'none';

        // Set the cursor at multiple levels
        self.gutter.style.cursor = cursor;
        self.parent.style.cursor = cursor;
        document$1.body.style.cursor = cursor;

        // Cache the initial sizes of the pair.
        calculateSizes.call(self);

        // Determine the position of the mouse compared to the gutter
        self.dragOffset = getMousePosition(e) - self.end;
    }

    // adjust sizes to ensure percentage is within min size and gutter.
    sizes = trimToMin(sizes);

    // 5. Create pair and element objects. Each pair has an index reference to
    // elements `a` and `b` of the pair (first and second elements).
    // Loop through the elements while pairing them off. Every pair gets a
    // `pair` object and a gutter.
    //
    // Basic logic:
    //
    // - Starting with the second element `i > 0`, create `pair` objects with
    //   `a = i - 1` and `b = i`
    // - Set gutter sizes based on the _pair_ being first/last. The first and last
    //   pair have gutterSize / 2, since they only have one half gutter, and not two.
    // - Create gutter elements and add event listeners.
    // - Set the size of the elements, minus the gutter sizes.
    //
    // -----------------------------------------------------------------------
    // |     i=0     |         i=1         |        i=2       |      i=3     |
    // |             |                     |                  |              |
    // |           pair 0                pair 1             pair 2           |
    // |             |                     |                  |              |
    // -----------------------------------------------------------------------
    var pairs = [];
    elements = ids.map(function (id, i) {
        // Create the element object.
        var element = {
            element: elementOrSelector(id),
            size: sizes[i],
            minSize: minSizes[i],
            maxSize: maxSizes[i],
            i: i,
        };

        var pair;

        if (i > 0) {
            // Create the pair object with its metadata.
            pair = {
                a: i - 1,
                b: i,
                dragging: false,
                direction: direction,
                parent: parent,
            };

            pair[aGutterSize] = getGutterSize(
                gutterSize,
                i - 1 === 0,
                false,
                gutterAlign
            );
            pair[bGutterSize] = getGutterSize(
                gutterSize,
                false,
                i === ids.length - 1,
                gutterAlign
            );

            // if the parent has a reverse flex-direction, switch the pair elements.
            if (
                parentFlexDirection === 'row-reverse' ||
                parentFlexDirection === 'column-reverse'
            ) {
                var temp = pair.a;
                pair.a = pair.b;
                pair.b = temp;
            }
        }

        // Determine the size of the current element. IE8 is supported by
        // staticly assigning sizes without draggable gutters. Assigns a string
        // to `size`.
        //
        // Create gutter elements for each pair.
        if (i > 0) {
            var gutterElement = gutter(i, direction, element.element);
            setGutterSize(gutterElement, gutterSize, i);

            // Save bound event listener for removal later
            pair[gutterStartDragging] = startDragging.bind(pair);

            // Attach bound event listener
            gutterElement[addEventListener](
                'mousedown',
                pair[gutterStartDragging]
            );
            gutterElement[addEventListener](
                'touchstart',
                pair[gutterStartDragging]
            );

            parent.insertBefore(gutterElement, element.element);

            pair.gutter = gutterElement;
        }

        setElementSize(
            element.element,
            element.size,
            getGutterSize(
                gutterSize,
                i === 0,
                i === ids.length - 1,
                gutterAlign
            ),
            i
        );

        // After the first iteration, and we have a pair object, append it to the
        // list of pairs.
        if (i > 0) {
            pairs.push(pair);
        }

        return element
    });

    function adjustToMin(element) {
        var isLast = element.i === pairs.length;
        var pair = isLast ? pairs[element.i - 1] : pairs[element.i];

        calculateSizes.call(pair);

        var size = isLast
            ? pair.size - element.minSize - pair[bGutterSize]
            : element.minSize + pair[aGutterSize];

        adjust.call(pair, size);
    }

    elements.forEach(function (element) {
        var computedSize = element.element[getBoundingClientRect]()[dimension];

        if (computedSize < element.minSize) {
            if (expandToMin) {
                adjustToMin(element);
            } else {
                // eslint-disable-next-line no-param-reassign
                element.minSize = computedSize;
            }
        }
    });

    function setSizes(newSizes) {
        var trimmed = trimToMin(newSizes);
        trimmed.forEach(function (newSize, i) {
            if (i > 0) {
                var pair = pairs[i - 1];

                var a = elements[pair.a];
                var b = elements[pair.b];

                a.size = trimmed[i - 1];
                b.size = newSize;

                setElementSize(a.element, a.size, pair[aGutterSize], a.i);
                setElementSize(b.element, b.size, pair[bGutterSize], b.i);
            }
        });
    }

    function destroy(preserveStyles, preserveGutter) {
        pairs.forEach(function (pair) {
            if (preserveGutter !== true) {
                pair.parent.removeChild(pair.gutter);
            } else {
                pair.gutter[removeEventListener](
                    'mousedown',
                    pair[gutterStartDragging]
                );
                pair.gutter[removeEventListener](
                    'touchstart',
                    pair[gutterStartDragging]
                );
            }

            if (preserveStyles !== true) {
                var style = elementStyle(
                    dimension,
                    pair.a.size,
                    pair[aGutterSize]
                );

                Object.keys(style).forEach(function (prop) {
                    elements[pair.a].element.style[prop] = '';
                    elements[pair.b].element.style[prop] = '';
                });
            }
        });
    }

    return {
        setSizes: setSizes,
        getSizes: getSizes,
        collapse: function collapse(i) {
            adjustToMin(elements[i]);
        },
        destroy: destroy,
        parent: parent,
        pairs: pairs,
    }
};

const splitterCss = ":host{display:block;width:100%;height:100%;overflow:hidden}:host ::slotted(.gutter-horizontal),:host ::slotted(.gutter-vertical){-webkit-transition:background-color 0.25s;transition:background-color 0.25s;background-color:var(--gray-01)}:host ::slotted(.gutter-horizontal){height:100%;float:left;cursor:col-resize;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;position:relative}:host ::slotted(.gutter-horizontal):after{right:-5px}:host ::slotted(.gutter-horizontal):before{left:-5px}:host ::slotted(.gutter-vertical){cursor:row-resize;display:-ms-flexbox;display:flex;-ms-flex-pack:center;justify-content:center;-ms-flex-align:center;align-items:center;background-color:var(--gray-01);position:relative}:host ::slotted(.gutter-vertical):after{right:50%;-webkit-transform:rotate(90deg) translateX(6px) translateY(-2px);transform:rotate(90deg) translateX(6px) translateY(-2px)}:host ::slotted(.gutter-vertical):before{left:50%;-webkit-transform:rotate(90deg) translateX(-6px) translateY(2px);transform:rotate(90deg) translateX(-6px) translateY(2px)}:host([knob=bidirectional]) ::slotted(.gutter-horizontal):after,:host([knob=bidirectional]) ::slotted(.gutter-vertical):after{content:\"\";width:0;height:0;border-top:4px solid transparent;border-bottom:4px solid transparent;border-left:4px solid var(--gray-06);z-index:10;position:absolute;pointer-events:none}:host([knob=bidirectional]) ::slotted(.gutter-horizontal):before,:host([knob=bidirectional]) ::slotted(.gutter-vertical):before{content:\"\";width:0;height:0;border-top:4px solid transparent;border-bottom:4px solid transparent;border-right:4px solid var(--gray-06);z-index:10;position:absolute;pointer-events:none}:host(.knob-left-hover) ::slotted(.gutter-horizontal):before,:host(.knob-left-hover) ::slotted(.gutter-vertical):before{border-top:4px solid transparent;border-bottom:4px solid transparent;border-right:4px solid var(--color-primary-active)}:host(.knob-right-hover) ::slotted(.gutter-horizontal):after,:host(.knob-right-hover) ::slotted(.gutter-vertical):after{border-top:4px solid transparent;border-bottom:4px solid transparent;border-left:4px solid var(--color-primary-active)}:host(.gutter-reached-end) ::slotted(.gutter-horizontal),:host(.gutter-reached-end) ::slotted(.gutter-vertical){background-color:var(--color-primary-active)}:host(.collapsed-to-zero) ::slotted(.gutter){pointer-events:none}:host(.collapsed-to-zero) ::slotted(.knob){pointer-events:auto}:host(.gutter-reached-right[knob=bidirectional]) ::slotted(.gutter-horizontal):after,:host(.gutter-reached-right[knob=bidirectional]) ::slotted(.gutter-vertical):after{display:none}:host(.gutter-reached-left[knob=bidirectional]) ::slotted(.gutter-horizontal):before,:host(.gutter-reached-left[knob=bidirectional]) ::slotted(.gutter-vertical):before{display:none}";

const GxgSplitter = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.dragging = index.createEvent(this, "dragging", 7);
        this.dragEnded = index.createEvent(this, "dragEnded", 7);
        /**
         * The splitter direction
         */
        this.direction = "horizontal";
        /**
         * The splitter direction
         */
        this.forceCollapseZero = false;
        /**
         * The type of knob
         */
        this.knob = "none";
        /**
         * The splitter min. sizes in pixels
         */
        this.minSize = "0,0";
        /**
         * The splitter initial sizes, in percentages. The sum should equal 100
         */
        this.sizes = "50,50";
        this.idsArray = [];
        this.leftSplitReachedMin = false;
        this.rightSplitReachedMin = false;
        this.collapsedToZero = false;
    }
    makeId(length) {
        const result = [];
        const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        const charactersLength = characters.length;
        for (let i = 0; i < length; i++) {
            result.push(characters.charAt(Math.floor(Math.random() * charactersLength)));
        }
        result.unshift("a"); // this prevents the id begining with a number, which is not allowed
        return result.join("");
    }
    componentWillLoad() {
        const splits = this.el.querySelectorAll("gxg-split");
        splits.forEach(function (split) {
            const randomId = this.makeId(5);
            split.setAttribute("id", randomId);
        }.bind(this));
    }
    componentDidLoad() {
        this.getIds();
        this.convertStringPropertiesToArray();
        this.validateSizes();
        this.split = Split(this.idsArray, {
            gutterSize: 8,
            cursor: "col-resize",
            minSize: this.minSizeArray,
            sizes: this.sizesArray,
            direction: this.direction,
            onDragStart: this.onDragStartFunc.bind(this),
            onDrag: this.onDragFunc.bind(this),
            onDragEnd: this.onDragEndFunc.bind(this),
            gutter: () => {
                const gutter = document.createElement("div");
                if (this.knob === "bidirectional") {
                    //KNOB
                    const knob = document.createElement("span");
                    knob.classList.add("knob");
                    knob.style.backgroundColor = `var(--gray-01)`;
                    knob.style.width = `var(--spacing-comp-05)`;
                    knob.style.height = `var(--spacing-comp-05)`;
                    knob.style.display = "block";
                    knob.style.zIndex = "5";
                    knob.style.position = "absolute";
                    knob.style.borderRadius = "50%";
                    knob.style.cursor = "pointer";
                    if (this.direction === "vertical") {
                        knob.style.transform = "rotate(90deg)";
                    }
                    //KNOB middle line
                    const knobMiddleLine = document.createElement("span");
                    knobMiddleLine.style.backgroundColor = `var(--gray-06)`;
                    knobMiddleLine.style.width = "2px";
                    knobMiddleLine.style.height = "16px";
                    knobMiddleLine.style.position = "relative";
                    knobMiddleLine.style.left = "11px";
                    knobMiddleLine.style.top = "4px";
                    knobMiddleLine.style.display = "block";
                    knob.appendChild(knobMiddleLine);
                    //KNOB left side
                    const knobLeftSide = document.createElement("span");
                    knobLeftSide.style.width = "11px";
                    knobLeftSide.style.height = `var(--spacing-comp-05)`;
                    knobLeftSide.style.display = "block";
                    knobLeftSide.style.top = "-16px";
                    knobLeftSide.style.position = "relative";
                    knobLeftSide.style.borderBottomLeftRadius = "40px";
                    knobLeftSide.style.borderTopLeftRadius = "40px";
                    knobLeftSide.style.backgroundColor = "transparent";
                    knobLeftSide.style.cursor = "pointer";
                    knobLeftSide.style.zIndex = "20";
                    knobLeftSide.style.pointerEvents = "auto";
                    knobLeftSide.addEventListener("click", this.knobLeftClicked.bind(this));
                    knobLeftSide.onmouseover = this.knobLeftOver.bind(this);
                    knobLeftSide.onmouseout = this.knobLeftOut.bind(this);
                    knob.appendChild(knobLeftSide);
                    //KNOB right side
                    const knobRightSide = document.createElement("span");
                    knobRightSide.style.width = "11px";
                    knobRightSide.style.height = `var(--spacing-comp-05)`;
                    knobRightSide.style.display = "block";
                    knobRightSide.style.top = "-40px";
                    knobRightSide.style.right = "-13px";
                    knobRightSide.style.position = "relative";
                    knobRightSide.style.borderBottomRightRadius = "40px";
                    knobRightSide.style.borderTopRightRadius = "40px";
                    knobRightSide.style.backgroundColor = "transparent";
                    knobRightSide.style.cursor = "pointer";
                    knobRightSide.style.zIndex = "20";
                    knobRightSide.style.pointerEvents = "auto";
                    knobRightSide.addEventListener("click", this.knobRightClicked.bind(this));
                    knobRightSide.onmouseover = this.knobRightOver.bind(this);
                    knobRightSide.onmouseout = this.knobRightOut.bind(this);
                    knob.appendChild(knobRightSide);
                    gutter.appendChild(knob);
                    //End of KNOB
                }
                gutter.className = `gutter gutter-${this.direction}`;
                return gutter;
            },
        });
        //set classes
        const slottedSplits = this.el.querySelectorAll("gxg-split");
        if (this.direction === "horizontal") {
            slottedSplits.forEach(function (split) {
                split.classList.add("split-horizontal");
            });
        }
    }
    convertStringPropertiesToArray() {
        this.minSizeArray = this.minSize.split(",").map(function (number) {
            return parseInt(number, 10);
        });
        this.sizesArray = this.sizes.split(",").map(function (number) {
            return parseInt(number, 10);
        });
    }
    getIds() {
        const slottedSplits = this.el.querySelectorAll("gxg-split");
        slottedSplits.forEach(function (split) {
            const splitId = split.getAttribute("id");
            this.idsArray.push("#" + splitId);
        }.bind(this));
    }
    validateSizes() {
        let totalSize = 0;
        let eachSplitSize = 0;
        const sizesArrayLength = this.sizesArray.length;
        this.sizesArray.forEach(function (splitSize) {
            totalSize += splitSize;
        });
        if (totalSize > 100) {
            eachSplitSize = 100 / sizesArrayLength;
            this.sizesArray = [];
            for (let i = 0; i < sizesArrayLength; i++) {
                this.sizesArray.push(eachSplitSize);
            }
        }
    }
    //KNOB
    knobLeftClicked() {
        //add class to make the transition smooth
        const slottedSplits = this.el.querySelectorAll("gxg-split");
        slottedSplits.forEach(function (split) {
            split.classList.add("smooth-transition");
        });
        if (this.forceCollapseZero) {
            if (this.collapsedToZero) {
                console.log("collapsed to zero");
                this.split.collapse(1);
                this.rightSplitReachedMin = true;
                this.collapsedToZero = false;
            }
            else if (this.leftSplitReachedMin) {
                console.log("left Split Reached Min");
                const leftSplit = slottedSplits[0];
                const rightSplit = slottedSplits[1];
                if (this.direction === "horizontal") {
                    leftSplit.style.width = "0";
                    rightSplit.style.width = "calc(100% - 8px)";
                    this.collapsedToZero = true;
                }
                else if (this.direction === "vertical") {
                    leftSplit.style.height = "0";
                    rightSplit.style.height = "calc(100% - 8px)";
                    this.collapsedToZero = true;
                }
            }
            else {
                this.split.collapse(0);
                this.leftSplitReachedMin = true;
                this.rightSplitReachedMin = false;
            }
        }
        else {
            this.split.collapse(0);
            setTimeout(function () {
                this.detectDragEndReachedMinimum();
            }.bind(this), 350 // This value has to the be same as transition speed on split.scss on the .smooth-transition class
            );
        }
    }
    knobRightClicked() {
        //add class to make the transition smooth
        const slottedSplits = this.el.querySelectorAll("gxg-split");
        slottedSplits.forEach(function (split) {
            split.classList.add("smooth-transition");
        });
        if (this.forceCollapseZero) {
            if (this.collapsedToZero) {
                console.log("collapsed to zero");
                this.split.collapse(0);
                this.rightSplitReachedMin = false;
                this.collapsedToZero = false;
            }
            else if (this.rightSplitReachedMin) {
                console.log("right Split Reached Min");
                const leftSplit = slottedSplits[0];
                const rightSplit = slottedSplits[1];
                if (this.direction === "horizontal") {
                    leftSplit.style.width = "calc(100% - 8px)";
                    rightSplit.style.width = "0";
                    this.collapsedToZero = true;
                }
                else if (this.direction === "vertical") {
                    leftSplit.style.height = "calc(100% - 8px)";
                    rightSplit.style.height = "0";
                    this.collapsedToZero = true;
                }
            }
            else {
                console.log("collapse 1");
                this.split.collapse(1);
                this.rightSplitReachedMin = true;
                this.leftSplitReachedMin = false;
            }
        }
        else {
            this.split.collapse(1);
            setTimeout(function () {
                this.detectDragEndReachedMinimum();
            }.bind(this), 350 // This value has to the be same as transition speed on split.scss on the .smooth-transition class
            );
        }
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
        //remove class that makes the transition smooth
        const slottedSplits = this.el.querySelectorAll("gxg-split");
        slottedSplits.forEach(function (split) {
            split.classList.remove("smooth-transition");
        });
    }
    onDragFunc() {
        this.detectDragEndReachedMinimum();
        //emmit dragging event
        this.dragging.emit("dragging");
    }
    onDragEndFunc() {
        setTimeout(function () {
            //Emmit drag ended event
            this.dragEnded.emit("drag ended");
        }.bind(this), 350 // This value has to the be same as transition speed on split.scss on the .smooth-transition class
        );
    }
    detectDragEndReachedMinimum() {
        let splitterLength;
        if (this.direction === "horizontal") {
            splitterLength = this.el.clientWidth;
        }
        else if (this.direction === "vertical") {
            splitterLength = this.el.clientHeight;
        }
        //actual sizes in percentages
        const rightActualSize = this.split.getSizes()[1];
        const leftActualSize = this.split.getSizes()[0];
        //actual sizes in pixels
        const rightActualSizePixels = Math.trunc((rightActualSize * splitterLength) / 100);
        const leftActualSizePixels = Math.trunc((leftActualSize * splitterLength) / 100);
        if ((rightActualSizePixels < this.minSizeArray[1] + 15 &&
            rightActualSizePixels > this.minSizeArray[1] - 15) ||
            (leftActualSizePixels < this.minSizeArray[0] + 15 &&
                leftActualSizePixels > this.minSizeArray[0] - 15)) {
            this.el.classList.add("gutter-reached-end");
            setTimeout(function () {
                this.el.classList.remove("gutter-reached-end");
            }.bind(this), 350 // This value has to the be same as transition speed on split.scss on the .smooth-transition class
            );
        }
        else {
            this.el.classList.remove("gutter-reached-left");
            this.el.classList.remove("gutter-reached-right");
            this.rightSplitReachedMin = false;
            this.leftSplitReachedMin = false;
        }
        //if guttter reached right:
        if (rightActualSizePixels < this.minSizeArray[1] + 15 &&
            rightActualSizePixels > this.minSizeArray[1] - 15) {
            if (!this.forceCollapseZero) {
                this.el.classList.add("gutter-reached-right");
                this.el.classList.remove("gutter-reached-left");
            }
            else {
                this.rightSplitReachedMin = true;
                this.leftSplitReachedMin = false;
            }
        }
        //if guttter reached left:
        if (leftActualSizePixels < this.minSizeArray[0] + 15 &&
            leftActualSizePixels > this.minSizeArray[0] - 15) {
            if (!this.forceCollapseZero) {
                this.el.classList.add("gutter-reached-left");
                this.el.classList.remove("gutter-reached-right");
            }
            else {
                this.leftSplitReachedMin = true;
                this.rightSplitReachedMin = false;
            }
        }
    }
    /**
     * This method allows to collapse the split passsed as argument
     */
    async collapse(split, forceCollapseZero = false) {
        //add class to make the transition smooth
        const slottedSplits = this.el.querySelectorAll("gxg-split");
        slottedSplits.forEach(function (split) {
            split.classList.add("smooth-transition");
        });
        const leftSplit = slottedSplits[0];
        const rightSplit = slottedSplits[1];
        if (this.forceCollapseZero && forceCollapseZero) {
            if (this.direction === "horizontal") {
                if (split === 0) {
                    leftSplit.style.width = "0";
                    rightSplit.style.width = "calc(100% - 8px)";
                }
                else if (split === 1) {
                    leftSplit.style.width = "calc(100% - 8px)";
                    rightSplit.style.width = "0";
                }
            }
            else if (this.direction === "vertical") {
                if (split === 0) {
                    leftSplit.style.height = "0";
                    rightSplit.style.height = "calc(100% - 8px)";
                }
                else if (split === 1) {
                    leftSplit.style.height = "calc(100% - 8px)";
                    rightSplit.style.height = "0";
                }
            }
            this.collapsedToZero = true;
        }
        else {
            this.split.collapse(split);
        }
    }
    render() {
        return (index.h(index.Host, { class: { "collapsed-to-zero": this.collapsedToZero } }, index.h("slot", null)));
    }
    get el() { return index.getElement(this); }
};
GxgSplitter.style = splitterCss;

exports.gxg_split = GxgSplit;
exports.gxg_splitter = GxgSplitter;
