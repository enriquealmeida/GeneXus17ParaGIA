
function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); if (enumerableOnly) symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; }); keys.push.apply(keys, symbols); } return keys; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; if (i % 2) { ownKeys(Object(source), true).forEach(function (key) { _defineProperty(target, key, source[key]); }); } else if (Object.getOwnPropertyDescriptors) { Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)); } else { ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } } return target; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _createForOfIteratorHelper(o, allowArrayLike) { var it; if (typeof Symbol === "undefined" || o[Symbol.iterator] == null) { if (Array.isArray(o) || (it = _unsupportedIterableToArray(o)) || allowArrayLike && o && typeof o.length === "number") { if (it) o = it; var i = 0; var F = function F() {}; return { s: F, n: function n() { if (i >= o.length) return { done: true }; return { done: false, value: o[i++] }; }, e: function e(_e) { throw _e; }, f: F }; } throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); } var normalCompletion = true, didErr = false, err; return { s: function s() { it = o[Symbol.iterator](); }, n: function n() { var step = it.next(); normalCompletion = step.done; return step; }, e: function e(_e2) { didErr = true; err = _e2; }, f: function f() { try { if (!normalCompletion && it["return"] != null) it["return"](); } finally { if (didErr) throw err; } } }; }

function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

// IE polyfills
if (!Array.prototype.flat) {
  Array.prototype.flat = function (depth) {
    var flattened = [];

    (function flat(array, depth) {
      var _iterator = _createForOfIteratorHelper(array),
          _step;

      try {
        for (_iterator.s(); !(_step = _iterator.n()).done;) {
          var el = _step.value;

          if (Array.isArray(el) && depth > 0) {
            flat(el, depth - 1);
          } else {
            flattened.push(el);
          }
        }
      } catch (err) {
        _iterator.e(err);
      } finally {
        _iterator.f();
      }
    })(this, Math.floor(depth) || 1);

    return flattened;
  };
}

if (!String.prototype.endsWith) {
  String.prototype.endsWith = function (search, this_len) {
    if (this_len === undefined || this_len > this.length) {
      this_len = this.length;
    }

    return this.substring(this_len - search.length, this_len) === search;
  };
}

if (!String.prototype.startsWith) {
  Object.defineProperty(String.prototype, 'startsWith', {
    value: function value(search, rawPos) {
      var pos = rawPos > 0 ? rawPos | 0 : 0;
      return this.substring(pos, pos + search.length) === search;
    }
  });
} ///////

//gx.forminspector(ctrl_gxid, row, gxobjectWC)
//ctrl_gxid is the control name as defined in Genexus
//row is a optional filter in case that the selected control is in a grid level
//gxobjectWC is an optional filter to do the search over a specific Genexus WebComponent
//Return: array of objects with each instance of the control with the specified GXId in the HTML form
//Each element has the form 
//"inMasterPage":boolean
//"cmp":string(id) if the controls is not in a component this value will be empty
//"id":string(controlDOMId)
//"value":string(controlvalue)
//"text":string(controlInnerText)

gx.forminspector = function (ctrl_gxid, row, gxobjectWC) {
    var gx_control_att = 'data-gx-control-name',
        gx_cmp_prefix_att = 'data-gx-cmp-prefix',
        gx_prompt_att = 'data-gx-prompt-name',
        gx_control_type_att = 'data-gx-control-type';

    var initialize_gx_object = function initialize_gx_object(gxo) {
      if (gxo && gxo.GXCtrlIds) {
        gxo.GXCtrlIds.map(function (i) {
          var vStruct = gxo.GXValidFnc[i];

          if (vStruct) {
            var gxName = '';

            if (vStruct.fld) {
              if (vStruct.gxvar && !vStruct.v2bc) {
                //Genexus Var or Att
                var bIsVar = vStruct.gxvar.startsWith('AV');
                gxName = "".concat(bIsVar ? '&' : '').concat(vStruct.gxvar.replace(/^AV?\d*/, ''));
              } else {
                gxName = vStruct.fld;
              }

              var rExp = new RegExp("(span_)?".concat(gxo.CmpContext).concat(vStruct.fld, "(_([0-9]{4})*)?$")),
                  prompt = gx.attachedControls.filter(function (attC) {
                return attC.info.isPrompt && attC.info.controls.slice(-1) == vStruct.id;
              });
              $('[id]').filter(function () {
                return $(this)[0].id.match(rExp);
              }).attr(gx_control_att, gxName.toLowerCase()).attr(gx_prompt_att, function () {
                return prompt.length ? prompt[0].id : null;
              }).attr(gx_control_type_att, vStruct.type);
              $('input[type=radio]').filter(function () {
                return $(this)[0].name.match(rExp);
              }).attr(gx_control_att, gxName.toLowerCase());
            }
          }
        });

            for (let wc in gxo.CmpControls) {
                $("#".concat(gxo.CmpContext, "gxHTMLWrp").concat(gxo.CmpControls[wc].Prefix)).attr(gx_control_att, gxo.CmpControls[wc].id.toLowerCase()).attr(gx_cmp_prefix_att, gxo.CmpControls[wc].Prefix);
            }
        }
    };

    var _init = function _init() {
      if (!gx.forminspector._initialized) {
        initialize_gx_object(gx.pO);
        initialize_gx_object(gx.pO.MasterPage);
        gx.pO.WebComponents.map(function (gxComponent) {
          return initialize_gx_object(gxComponent);
        });
        gx.fx.obs.addObserver('webcom.render', gx.forminspector, function (gxComponent) {
          return initialize_gx_object(gxComponent);
        });
        gx.forminspector._initialized = true;
      }
    };

    _init();

    var srow = row ? gx.text.padl(row.toString(), 4, '0') : undefined;
    var selector = "[".concat(gx_control_att, "=\"").concat(ctrl_gxid.toLowerCase(), "\"]");

    if (row) {
      selector = "[data-gxrow=\"".concat(srow, "\"] ") + selector;
    }

    var targets = gx.$("".concat(selector)),
        visibleTargets;

    if (targets.length > 1 && targets.attr(gx_control_type_att) !== 'bits') {
      visibleTargets = gx.$("".concat(selector, ":visible"));

      if (visibleTargets.length > 0) {
        targets = visibleTargets;
      }
    }

    var targetValue = function targetValue(target, gxO) {
      var excludedInputTypes = ['file'];

      if (target.tagName && target.tagName.toLowerCase() === 'input' && excludedInputTypes.indexOf(target.type) < 0) {
        var id = target.type === 'radio' ? target.name : target.id;
        return gx.fn.getControlValue_impl(id, undefined, gxO).toString();
      }

      return target.value;
    };

    let ret = gx.$.map(targets, function (target) {
        var inMasterPage = target.id.endsWith('_MPAGE'),
            cmpElementsArr = gx.$(target).closest('[class=gxwebcomponent]').map(function (i, el) {
                var gxCtrlName = gx.$(el).attr(gx_control_att);
                return !gxobjectWC || gxobjectWC.toLowerCase() === gxCtrlName ? {
                    isComponent: target === el,
                    cmpctrl_gxid: gxCtrlName,
                    cmpprefix: gx.$(el).attr(gx_cmp_prefix_att)
                } : null;
            }),
            cmpElement = cmpElementsArr.length ? cmpElementsArr[0] : undefined,
            gxO = cmpElement ? cmpElement : inMasterPage ? gx.pO.MasterPage : gx.pO,
            prompt = target.getAttribute(gx_prompt_att),
            prompt_row = target.id.match(/([0-9]{4})+/),
            prompt_row_id = prompt_row ? "_".concat(prompt_row[0]) : '',
            prompt_id = prompt ? "".concat(prompt).concat(prompt_row_id) : undefined,
            el = {
                inMasterPage: inMasterPage,
                id: target.id,
                text: target.textContent
            },
            ballonEl;

      if (prompt_id) {
        el.prompt = prompt_id;
      }

        el.isEnabled = !(target.hasAttribute('data-gx-readonly') || target.classList.contains('gx-disabled'));

      if (!cmpElement || cmpElement.length === 0) {
        el = gxobjectWC ? null : [_objectSpread(_objectSpread({}, el), {}, {
          validationText: (ballonEl = $("#".concat(el.id, "_Balloon")), ballonEl.is(':visible') ? ballonEl.text() : ''),
          value: targetValue(target, gxO),
          text: target.textContent,
          isComponent: false
        })];
      } else {
        gxO = gx.O.WebComponents[cmpElement.cmpprefix];
        el = [_objectSpread(_objectSpread({}, el), {}, {
          cmpctrl_gxid: cmpElement.cmpctrl_gxid,
          value: targetValue(target, gxO),
          text: target.textContent,
          isComponent: cmpElement.isComponent
        })];
      }

      return el;
    });
    gx.$(ret).each(function (i, el) {
      var nRows = gx.$("#".concat(el.id, " tr, #").concat(el.id, " div[class=row], #").concat(el.id, " div[data-gx-smarttable-cell], #").concat(el.id, " div[data-gx-canvas-cell]")).length;

      if (!el.isComponent && nRows > 0) {
        el.rows = nRows;
      }
    });
    return ret;
};

gx.inspector = {
    messages: function messages() {
      //Return array of GX messages (msg) in form
      return $.map($(".gx_ev, .ErrorMessages, .WarningMessages"), function (n, i) {
        var $n = $(n);
        return $n.text().length > 0 ? $n.text() : null;
      });
    },
    elements: function elements(opts) {
      //opts is an object with properties:
      //ctrl_gxid is the control name as defined in Genexus (Required)
      //row is a optional filter in case that then selected control is in a grid level (Optional)
      //gxobjectWC is an optional filter to do the search over a specific Genexus WebComponent (Optional)
      var _opts$ctrl_gxid = opts.ctrl_gxid,
          ctrl_gxid = _opts$ctrl_gxid === void 0 ? "" : _opts$ctrl_gxid,
          _opts$row = opts.row,
          row = _opts$row === void 0 ? "" : _opts$row,
          _opts$gxobjectWC = opts.gxobjectWC,
          gxobjectWC = _opts$gxobjectWC === void 0 ? "" : _opts$gxobjectWC;
      return gx.forminspector(ctrl_gxid, row, gxobjectWC).concat(gx.inspector.grids(opts));
    },
    grids: function grids(opts) {
      //opts is an object with properties:
      //ctrl_gxid is the control name as defined in Genexus (Required)
      //row is a optional filter in case that then selected control is in a grid level (Optional)
      //Row number shold be of length 4 with '0'left padding
      //0001001 row1 at parent row1 - 00010002 row2 at parent row1
      //gxobjectWC is an optional filter to do the search over a specific Genexus WebComponent (Optional)
      var _opts$ctrl_gxid2 = opts.ctrl_gxid,
          ctrl_gxid = _opts$ctrl_gxid2 === void 0 ? "" : _opts$ctrl_gxid2,
          _opts$row2 = opts.row,
          row = _opts$row2 === void 0 ? "" : _opts$row2,
          _opts$gxobjectWC2 = opts.gxobjectWC,
          gxobjectWC = _opts$gxobjectWC2 === void 0 ? "" : _opts$gxobjectWC2;

      var wcFilter = function wcFilter(wc) {
        var components = gx.forminspector(gxobjectWC.toLowerCase());
        return gxobjectWC && components.length > 0 && components[0].id.endsWith(wc.CmpContext) ? wc : null;
      };

      var gFilter = function gFilter(g) {
        return g.realGridName.toLowerCase() === ctrl_gxid.toLowerCase() && (!row || g.parentRow.gxId === row) ? g : null;
      },
          wcGrids = gx.pO.WebComponents.filter(wcFilter).map(function (wc) {
        return wc.Grids;
      }).flat(),
          mpGrids = gx.pO.MasterPage ? gx.pO.MasterPage.Grids : [],
          Grids = (!gxobjectWC ? gx.pO.Grids : []).concat(wcGrids).concat(mpGrids),
          retObj = function retObj(g) {
        var closestWC = gx.$("#" + g.getContainerControlId()).closest('[class=gxwebcomponent]'),
            wcId = closestWC.length > 0 ? closestWC[0].id : "";
        wcName = wcId ? gx.$("#" + wcId).attr('data-gx-control-name') : "";

        if (!gxobjectWC || wcName.toLowerCase() === gxobjectWC.toLowerCase()) {
          var _g$parentObject;

          return {
            id: g.getContainerControlId(),
            cmpctrl_gxid: wcName,
            rows: g.grid.rows.length,
            inMasterPage: ((_g$parentObject = g.parentObject) === null || _g$parentObject === void 0 ? void 0 : _g$parentObject.IsMasterPage) || false
          };
        }
      };

      return Grids.filter(gFilter).map(retObj);
    }
};