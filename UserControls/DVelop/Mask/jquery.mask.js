﻿(function(n,t,i){typeof define=="function"&&define.amd?define(["jquery"],n):typeof exports=="object"&&typeof Meteor=="undefined"?module.exports=n(require("jquery")):n(t||i)})(function(n){"use strict";var i=function(t,i,r){var u={invalid:[],getCaret:function(){try{var n,i=0,e=t.get(0),f=document.selection,r=e.selectionStart;return f&&navigator.appVersion.indexOf("MSIE 10")===-1?(n=f.createRange(),n.moveStart("character",-u.val().length),i=n.text.length):(r||r==="0")&&(i=r),i}catch(o){}},setCaret:function(n){try{if(t.is(":focus")){var i,r=t.get(0);r.setSelectionRange?r.setSelectionRange(n,n):(i=r.createTextRange(),i.collapse(!0),i.moveEnd("character",n),i.moveStart("character",n),i.select())}}catch(u){}},events:function(){t.on("keydown.mask",function(n){t.data("mask-keycode",n.keyCode||n.which);t.data("mask-previus-value",t.val());t.data("mask-previus-caret-pos",u.getCaret());u.maskDigitPosMapOld=u.maskDigitPosMap}).on(n.jMaskGlobals.useInput?"input.mask":"keyup.mask",u.behaviour).on("paste.mask drop.mask",function(){setTimeout(function(){t.keydown().keyup()},100)}).on("change.mask",function(){t.data("changed",!0)}).on("blur.mask",function(){e===u.val()||t.data("changed")||t.trigger("change");t.data("changed",!1)}).on("blur.mask",function(){e=u.val()}).on("focus.mask",function(t){r.selectOnFocus===!0&&n(t.target).select()}).on("focusout.mask",function(){r.clearIfNotMatch&&!o.test(u.val())&&u.val("")})},getRegexMask:function(){for(var u=[],r,e,h,s,n,o,t=0;t<i.length;t++)r=f.translation[i.charAt(t)],r?(e=r.pattern.toString().replace(/.{1}$|^.{1}/g,""),h=r.optional,s=r.recursive,s?(u.push(i.charAt(t)),n={digit:i.charAt(t),pattern:e}):u.push(!h&&!s?e:e+"?")):u.push(i.charAt(t).replace(/[-\/\\^$*+?.()|[\]{}]/g,"\\$&"));return o=u.join(""),n&&(o=o.replace(new RegExp("("+n.digit+"(.*"+n.digit+")?)"),"($1)?").replace(new RegExp(n.digit,"g"),n.pattern)),new RegExp(o)},destroyEvents:function(){t.off(["input","keydown","keyup","paste","drop","blur","focusout",""].join(".mask "))},val:function(n){var u=t.is("input"),i=u?"val":"text",r;return arguments.length>0?(t[i]()!==n&&t[i](n),r=t):r=t[i](),r},calculateCaretPosition:function(n){var s=u.getMasked(),i=u.getCaret(),v;if(n!==s){for(var f=t.data("mask-previus-caret-pos")||0,h=s.length,c=n.length,l=0,a=0,e=0,o=0,r=0,r=i;r<h;r++){if(!u.maskDigitPosMap[r])break;a++}for(r=i-1;r>=0;r--){if(!u.maskDigitPosMap[r])break;l++}for(r=i-1;r>=0;r--)u.maskDigitPosMap[r]&&e++;for(r=f-1;r>=0;r--)u.maskDigitPosMapOld[r]&&o++;i>c?i=h*10:f>=i&&f!==c?u.maskDigitPosMapOld[i]||(v=i,i-=o-e,i-=l,u.maskDigitPosMap[i]&&(i=v)):i>f&&(i+=e-o,i+=a)}return i},behaviour:function(i){var r;if(i=i||window.event,u.invalid=[],r=t.data("mask-keycode"),n.inArray(r,f.byPassKeys)===-1){var e=u.getMasked(),o=u.getCaret(),s=t.data("mask-previus-value")||"";return setTimeout(function(){u.setCaret(u.calculateCaretPosition(s))},n.jMaskGlobals.keyStrokeCompensation),u.val(e),u.setCaret(o),u.callbacks(i)}},getMasked:function(n,t){var c=[],rt=t===undefined?u.val():t+"",e=0,y=i.length,o=0,p=rt.length,s=1,w="push",l=-1,k=0,d=[],a,g,nt,tt,it;for(r.reverse?(w="unshift",s=-1,a=0,e=y-1,o=p-1,g=function(){return e>-1&&o>-1}):(a=y-1,g=function(){return e<y&&o<p});g();){var b=i.charAt(e),v=rt.charAt(o),h=f.translation[b];h?(v.match(h.pattern)?(c[w](v),h.recursive&&(l===-1?l=e:e===a&&e!==l&&(e=l-s),a===l&&(e-=s)),e+=s):v===nt?(k--,nt=undefined):h.optional?(e+=s,o-=s):h.fallback?(c[w](h.fallback),e+=s,o-=s):u.invalid.push({p:o,v:v,e:h.pattern}),o+=s):(n||c[w](b),v===b?(d.push(o),o+=s):(nt=b,d.push(o+k),k++),e+=s)}return tt=i.charAt(a),y!==p+1||f.translation[tt]||c.push(tt),it=c.join(""),u.mapMaskdigitPositions(it,d,p),it},mapMaskdigitPositions:function(n,t,i){var e=r.reverse?n.length-i:0,f;for(u.maskDigitPosMap={},f=0;f<t.length;f++)u.maskDigitPosMap[t[f]+e]=1},callbacks:function(n){var f=u.val(),h=f!==e,s=[f,n,t,r],o=function(n,t,i){typeof r[n]=="function"&&t&&r[n].apply(this,i)};o("onChange",h===!0,s);o("onKeyPress",h===!0,s);o("onComplete",f.length===i.length,s);o("onInvalid",u.invalid.length>0,[f,n,t,u.invalid,r])}},f,e,o;t=n(t);f=this;e=u.val();i=typeof i=="function"?i(u.val(),undefined,t,r):i;f.mask=i;f.options=r;f.remove=function(){var n=u.getCaret();return f.options.placeholder&&t.removeAttr("placeholder"),t.data("mask-maxlength")&&t.removeAttr("maxlength"),u.destroyEvents(),u.val(f.getCleanVal()),u.setCaret(n),t};f.getCleanVal=function(){return u.getMasked(!0)};f.getMaskedVal=function(n){return u.getMasked(!1,n)};f.init=function(e){var s,h,c,l;if(e=e||!1,r=r||{},f.clearIfNotMatch=n.jMaskGlobals.clearIfNotMatch,f.byPassKeys=n.jMaskGlobals.byPassKeys,f.translation=n.extend({},n.jMaskGlobals.translation,r.translation),f=n.extend(!0,{},f,r),o=u.getRegexMask(),e)u.events(),u.val(u.getMasked());else{for(r.placeholder&&t.attr("placeholder",r.placeholder),t.data("mask")&&t.attr("autocomplete","off"),s=0,h=!0;s<i.length;s++)if(c=f.translation[i.charAt(s)],c&&c.recursive){h=!1;break}h&&t.attr("maxlength",i.length).data("mask-maxlength",!0);u.destroyEvents();u.events();l=u.getCaret();u.val(u.getMasked());u.setCaret(l)}};f.init(!t.is("input"))},t;n.maskWatchers={};var u=function(){var t=n(this),u={},f="data-mask-",e=t.attr("data-mask");return t.attr(f+"reverse")&&(u.reverse=!0),t.attr(f+"clearifnotmatch")&&(u.clearIfNotMatch=!0),t.attr(f+"selectonfocus")==="true"&&(u.selectOnFocus=!0),r(t,e,u)?t.data("mask",new i(this,e,u)):void 0},r=function(t,i,r){r=r||{};var u=n(t).data("mask"),f=JSON.stringify,e=n(t).val()||n(t).text();try{return typeof i=="function"&&(i=i(e)),typeof u!="object"||f(u.options)!==f(r)||u.mask!==i}catch(o){}},f=function(n){var t=document.createElement("div"),i;return n="on"+n,i=n in t,i||(t.setAttribute(n,"return;"),i=typeof t[n]=="function"),t=null,i};n.fn.mask=function(t,u){u=u||{};var f=this.selector,e=n.jMaskGlobals,s=e.watchInterval,h=u.watchInputs||e.watchInputs,o=function(){if(r(this,t,u))return n(this).data("mask",new i(this,t,u))};return n(this).each(o),f&&f!==""&&h&&(clearInterval(n.maskWatchers[f]),n.maskWatchers[f]=setInterval(function(){n(document).find(f).each(o)},s)),this};n.fn.masked=function(n){return this.data("mask").getMaskedVal(n)};n.fn.unmask=function(){return clearInterval(n.maskWatchers[this.selector]),delete n.maskWatchers[this.selector],this.each(function(){var t=n(this).data("mask");t&&t.remove().removeData("mask")})};n.fn.cleanVal=function(){return this.data("mask").getCleanVal()};n.applyDataMask=function(t){t=t||n.jMaskGlobals.maskElements;var i=t instanceof n?t:n(t);i.filter(n.jMaskGlobals.dataMaskAttr).each(u)};t={maskElements:"input,td,span,div",dataMaskAttr:"[data-mask]",dataMask:!0,watchInterval:300,watchInputs:!0,keyStrokeCompensation:10,useInput:!/Chrome\/[2-4][0-9]|SamsungBrowser/.test(window.navigator.userAgent)&&f("input"),watchDataMask:!1,byPassKeys:[9,16,17,18,36,37,38,39,40,91],translation:{"0":{pattern:/\d/},"9":{pattern:/\d/,optional:!0},"#":{pattern:/\d/,recursive:!0},A:{pattern:/[a-zA-Z0-9]/},S:{pattern:/[a-zA-Z]/}}};n.jMaskGlobals=n.jMaskGlobals||{};t=n.jMaskGlobals=n.extend(!0,{},t,n.jMaskGlobals);t.dataMask&&n.applyDataMask();setInterval(function(){n.jMaskGlobals.watchDataMask&&n.applyDataMask()},t.watchInterval)},window.jQuery,window.Zepto);$(window).one('load',function(){WWP_VV([['WorkWithPlusUtilities','14.3000'],['WorkWithPlusUtilities_F5','14.3000']]);});