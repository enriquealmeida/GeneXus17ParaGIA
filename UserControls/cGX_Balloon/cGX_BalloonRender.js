function cGX_Balloon() {
    this.ballonType;
    this.hb5;
    var a = this;
    gx.util.balloon.impl = function () {
        this.init = function (b) {
            this.hide();
            if (a.hb5) {
                a.hb5.hide()
            }
            this.id = b;
            this.balloonid = b + "_Balloon";
            this.hasMessage = false;
            this.messageErr = "";
            this.messageWar = "";
            this.isError = false;
            a.isError = false
        };
        this.setMessage = function (b) {
            this.messageWar += b + "<BR>";
            this.hasMessage = true
        };
        this.setError = function (b) {
            if (a.balloonType == "alert") {
                this.messageErr += b
            } else {
                this.messageErr += b + "<BR>"
            }
            this.isError = true;
            this.hasMessage = true;
            a.isError = true
        };
        this.hide = function () {
            try {
                ctrl = gx.dom.byId(this.balloonid + "Shadow");
                if (ctrl != null) {
                    gx.dom.removeControl(ctrl)
                }
                ctrl = gx.dom.byId(this.balloonid + "Shadow2");
                if (ctrl != null) {
                    gx.dom.removeControl(ctrl)
                }
                ctrl = gx.dom.byId(this.balloonid);
                if (ctrl != null) {
                    gx.dom.removeControl(ctrl)
                }
                ctrl = gx.dom.byId(this.balloonid + "GXiFrameIEHack");
                if (ctrl != null) {
                    gx.dom.removeControl(ctrl)
                }
            } catch (b) {
                gx.dbg.logEx(b, "gxballoon.js", "hide")
            }
        };
        this.defaultBalloon = function () {
            if (document.readyState != undefined && document.readyState != "complete") {
                return
            }
            try {
                if (this.hasMessage == false) {
                    return true
                }
                var h = gx.dom.el(this.balloonid);
                var b = gx.dom.el(this.id);
                if ((b == null) && (gx.csv.validatingUC != null)) {
                    b = gx.csv.validatingUC.getContainerControl()
                }
                if (b.type == "hidden") {
                    var c = gx.dom.el("span_" + this.id);
                    if (c != null) {
                        b = c
                    }
                }
                if (h == null) {
                    h = document.createElement("SPAN");
                    BalloonControlShadow = document.createElement("SPAN");
                    BalloonControlShadow2 = document.createElement("SPAN");
                    IFrameControl = document.createElement("IFRAME");
                    h.id = this.balloonid;
                    BalloonControlShadow.id = this.balloonid + "Shadow";
                    BalloonControlShadow2.id = this.balloonid + "Shadow2";
                    IFrameControl.id = this.balloonid + "GXiFrameIEHack";
                    IFrameControl.src = "javascript:undefined";
                    IFrameControl.style.zIndex = 1;
                    IFrameControl.style.visibility = "hidden";
                    IFrameControl.style.position = "absolute";
                    IFrameControl.frameBorder = "0";
                    document.body.appendChild(h);
                    document.body.appendChild(BalloonControlShadow);
                    document.body.appendChild(BalloonControlShadow2);
                    document.body.appendChild(IFrameControl)
                }
                var e = gx.dom.position(b);
                if (e.x == 0 && e.y == 0 && b.parentNode) {
                    e = gx.dom.position(b.parentNode)
                }
                var d = parseInt(e.x);
                var i = parseInt(e.y);
                if (d <= 0) {
                    d = 10
                }
                if (i <= 0) {
                    i = 20
                }
                var g = "<span style='white-space:nowrap;position: absolute; top:" + i + "px; left:" + d + "px;z-index:1003' >";
                if (this.messageErr.length > 0) {
                    g += "<div class='ErrorMessages'>";
                    g += this.messageErr;
                    g += "</div><span style='height:4px;overflow:hidden'></span>"
                }
                if (this.messageWar.length > 0) {
                    g += "<div class='WarningMessages'>";
                    g += this.messageWar;
                    g += "</div>"
                }
                g += "</span>";
                h.innerHTML = g;
                var f = 2;
                i -= h.lastChild.offsetHeight - f;
                g = "<span style='white-space:nowrap;position: absolute; top:" + i + "px; left:" + d + "px;z-index:1003' >";
                if (this.messageErr.length > 0) {
                    g += "<div class='ErrorMessages'>";
                    g += this.messageErr;
                    g += "</div><span style='height:4px;overflow:hidden'></span>"
                }
                if (this.messageWar.length > 0) {
                    g += "<div class='WarningMessages'>";
                    g += this.messageWar;
                    g += "</div>"
                }
                g += "</span>";
                h.innerHTML = g;
                IFrameControl.style.visibility = "visible";
                IFrameControl.style.height = h.firstChild.firstChild.offsetHeight;
                IFrameControl.style.width = h.lastChild.offsetWidth;
                IFrameControl.style.top = i;
                IFrameControl.style.left = d;
                i += f;
                d += f;
                if (this.messageErr.length > 0) {
                    g = "<span style='position: absolute;border-style:none;";
                    g += "top:" + i + "px; left:" + d + "px;";
                    g += "width:" + h.lastChild.offsetWidth + "px; height:" + h.firstChild.firstChild.offsetHeight + "px;";
                    g += "z-index:1002;background-color:ThreeDDarkShadow;' class='ErrorMessages'></span>";
                    BalloonControlShadow.innerHTML = g;
                    i += h.firstChild.firstChild.offsetHeight + 4
                }
                if (this.messageWar.length > 0) {
                    g = "<span style='position: absolute;border-style:none;";
                    g += "top:" + i + "px; left:" + d + "px;";
                    g += "width:" + h.lastChild.offsetWidth + "px; height:" + h.firstChild.lastChild.offsetHeight + "px;";
                    g += "z-index:1002;background-color:ThreeDDarkShadow;' class='ErrorMessages'></span>";
                    BalloonControlShadow2.innerHTML = g
                }
                if (gx.util.balloon.timerOn == false) {
                    gx.util.balloon.timerOn = true;
                    this.timerId = setTimeout(function () {
                        gx.util.balloon.hideOnTime()
                    }, 4000)
                }
            } catch (j) {
                gx.dbg.logEx(j, "gxballoon.js", "show")
            }
            return !this.isError
        };
        this.show = function () {
            var b = this;
            if (!a.isError) {
                return true
            }
            if (a.balloonType == "customright") {
                a.hb5 = new HelpBalloon({
                    title: a.ParentObject.ServerClass,
                    content: this.messageErr,
                    icon: $(b.id),
                    cacheRemoteContent: false,
                    useEvent: []
                });
                a.hb5.show();
                if (gx.util.balloon.timerOn == false) {
                    gx.util.balloon.timerOn = true;
                    this.timerId = setTimeout(function () {
                        gx.util.balloon.timerOn = false;
                        a.isError = false;
                        a.hb5.hide()
                    }, 2000)
                }
            } else {
                if (a.balloonType == "default") {
                    this.defaultBalloon()
                } else {
                    if (a.balloonType == "alert") {
                        alert(this.messageErr)
                    }
                }
            }
            return false
        }
    };
    this.show = function () {
        var b = document.createElement("input");
        b.type = "text";
        b.id = "dummy";
        b.name = "dummy";
        gx.dom.form().appendChild(b);
        var c = new HelpBalloon({
            title: "Non-Ajax Balloon",
            content: "This is an example of static balloon content.",
            icon: b,
            autoHideTimeout: 2000
        });
        c.hide();
        gx.dom.form().removeChild(b)
    }
};