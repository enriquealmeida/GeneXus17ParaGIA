﻿function WWP_Chronometer(){this.show=function(){this.Start()};this.Start=function(){if(this.Tick!=null&&this.myTimer==null){var n=this;this.OnlyIfFormChanged&&(this.lastState=$("#"+WWP_getCurrentWCId(n)+"TABLEMAIN").find("input").serialize());this.myTimer=setInterval(function(){n.OnlyIfFormChanged&&n.lastState==(n.lastState=$("#"+WWP_getCurrentWCId(n)+"TABLEMAIN").find("input").serialize())||n.Tick()},this.TickInterval*1e3)}};this.Stop=function(){this.myTimer!=null&&(clearInterval(this.myTimer),delete this.myTimer);this.lastState!=null&&delete this.lastState};this.destroy=function(){this.Stop()}};$(window).one('load',function(){WWP_VV([['WWP.Chronometer','14.3000']]);});