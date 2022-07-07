Ext.data.JsonP.gxui_Message({"tagname":"class","name":"gxui.Message","autodetected":{"aliases":true,"alternateClassNames":true,"extends":true,"mixins":true,"requires":true,"uses":true,"members":true,"code_type":true},"files":[{"filename":"MessageRender.js","href":"MessageRender.html#gxui-Message"}],"aliases":{},"alternateClassNames":[],"extends":"Ext.Base","mixins":[],"requires":[],"uses":[],"members":[{"name":"Cls","tagname":"property","owner":"gxui.Message","id":"property-Cls","meta":{}},{"name":"Duration","tagname":"property","owner":"gxui.Message","id":"property-Duration","meta":{}},{"name":"Icon","tagname":"property","owner":"gxui.Message","id":"property-Icon","meta":{}},{"name":"Message","tagname":"property","owner":"gxui.Message","id":"property-Message","meta":{}},{"name":"Position","tagname":"property","owner":"gxui.Message","id":"property-Position","meta":{}},{"name":"Show","tagname":"property","owner":"gxui.Message","id":"property-Show","meta":{}},{"name":"Title","tagname":"property","owner":"gxui.Message","id":"property-Title","meta":{}},{"name":"Type","tagname":"property","owner":"gxui.Message","id":"property-Type","meta":{}},{"name":"ShowMessage","tagname":"method","owner":"gxui.Message","id":"method-ShowMessage","meta":{}}],"code_type":"ext_define","id":"class-gxui.Message","short_doc":"This control allows you to show a message in two ways: Alert or Notification. ...","component":false,"superclasses":["Ext.Base"],"subclasses":[],"mixedInto":[],"parentMixins":[],"html":"<div><pre class=\"hierarchy\"><h4>Hierarchy</h4><div class='subclass first-child'>Ext.Base<div class='subclass '><strong>gxui.Message</strong></div></div><h4>Files</h4><div class='dependency'><a href='source/MessageRender.html#gxui-Message' target='_blank'>MessageRender.js</a></div></pre><div class='doc-contents'><p>This control allows you to show a message in two ways: Alert or Notification.\nIf you want to show an alert, a modal message dialog will appear.\nIf you want to show a notification, a non-modal notification window will appear, similar to the one used by Microsoft Outlook when a new mail message arrives.</p>\n</div><div class='members'><div class='members-section'><div class='definedBy'>Defined By</div><h3 class='members-title icon-property'>Properties</h3><div class='subsection'><div id='property-Cls' class='member first-child not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/Properties.html#gxui-Message-property-Cls' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-property-Cls' class='name expandable'>Cls</a> : Text<span class=\"signature\"></span></div><div class='description'><div class='short'><p>An optional extra CSS class that will be added to this control's element.</p>\n</div><div class='long'><p>An optional extra CSS class that will be added to this control's element.</p>\n</div></div></div><div id='property-Duration' class='member  not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/Properties.html#gxui-Message-property-Duration' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-property-Duration' class='name expandable'>Duration</a> : Integer<span class=\"signature\"></span></div><div class='description'><div class='short'>Duration of the animation used to show the notification, in seconds. ...</div><div class='long'><p>Duration of the animation used to show the notification, in seconds.</p>\n<p>Defaults to: <code>1</code></p></div></div></div><div id='property-Icon' class='member  not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/Properties.html#gxui-Message-property-Icon' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-property-Icon' class='name expandable'>Icon</a> : Combo<span class=\"signature\"></span></div><div class='description'><div class='short'>Indicates which icon to use in the dialog. ...</div><div class='long'><p>Indicates which icon to use in the dialog.\nPossible values:</p>\n\n<ul>\n<li>Info</li>\n<li>Error</li>\n<li>Question</li>\n<li>Warning</li>\n</ul>\n\n<p>Defaults to: <code>Info</code></p></div></div></div><div id='property-Message' class='member  not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/Properties.html#gxui-Message-property-Message' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-property-Message' class='name expandable'>Message</a> : Text<span class=\"signature\"></span></div><div class='description'><div class='short'>Text of the message. ...</div><div class='long'><p>Text of the message.</p>\n<p>Defaults to: <code>This is the message</code></p></div></div></div><div id='property-Position' class='member  not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/Properties.html#gxui-Message-property-Position' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-property-Position' class='name expandable'>Position</a> : Combo<span class=\"signature\"></span></div><div class='description'><div class='short'>Screen position where the notification will be shown. ...</div><div class='long'><p>Screen position where the notification will be shown.\nPossible values:</p>\n\n<ul>\n<li>Top Left</li>\n<li>Top Center</li>\n<li>Top Right</li>\n<li>Center Left</li>\n<li>Center Center</li>\n<li>Center Right</li>\n<li>Bottom Left</li>\n<li>Bottom Center</li>\n<li>Bottom Right</li>\n</ul>\n\n<p>Defaults to: <code>Top Center</code></p></div></div></div><div id='property-Show' class='member  not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/Properties.html#gxui-Message-property-Show' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-property-Show' class='name expandable'>Show</a> : Combo<span class=\"signature\"></span></div><div class='description'><div class='short'>Deprecated. ...</div><div class='long'><p>Deprecated. If true, when a user event is executed, the message is shown. Use <a href=\"#!/api/gxui.Message-method-ShowMessage\" rel=\"gxui.Message-method-ShowMessage\" class=\"docClass\">ShowMessage</a> method instead.\nPossible values:</p>\n\n<ul>\n<li>False</li>\n<li>True</li>\n</ul>\n\n<p>Defaults to: <code>False</code></p></div></div></div><div id='property-Title' class='member  not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/Properties.html#gxui-Message-property-Title' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-property-Title' class='name expandable'>Title</a> : Text<span class=\"signature\"></span></div><div class='description'><div class='short'>Title of the message. ...</div><div class='long'><p>Title of the message.</p>\n<p>Defaults to: <code>Title</code></p></div></div></div><div id='property-Type' class='member  not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/Properties.html#gxui-Message-property-Type' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-property-Type' class='name expandable'>Type</a> : Combo<span class=\"signature\"></span></div><div class='description'><div class='short'>Indicates the way messages will be shown by default. ...</div><div class='long'><p>Indicates the way messages will be shown by default.\nIf you want to show an alert, a modal message dialog will appear.\nIf you want to show a notification, a non-modal notification window will appear, similar to the one used by Microsoft Outlook when a new mail message arrives.</p>\n\n<p>Possible values:</p>\n\n<ul>\n<li>Alert</li>\n<li>Notification</li>\n</ul>\n\n<p>Defaults to: <code>Alert</code></p></div></div></div></div></div><div class='members-section'><div class='definedBy'>Defined By</div><h3 class='members-title icon-method'>Methods</h3><div class='subsection'><div id='method-ShowMessage' class='member first-child not-inherited'><a href='#' class='side expandable'><span>&nbsp;</span></a><div class='title'><div class='meta'><span class='defined-in' rel='gxui.Message'>gxui.Message</span><br/><a href='source/MessageRender.html#gxui-Message-method-ShowMessage' target='_blank' class='view-source'>view source</a></div><a href='#!/api/gxui.Message-method-ShowMessage' class='name expandable'>ShowMessage</a>( <span class='pre'>[title], [message], [type]</span> )<span class=\"signature\"></span></div><div class='description'><div class='short'>Shows the message. ...</div><div class='long'><p>Shows the message.</p>\n<h3 class=\"pa\">Parameters</h3><ul><li><span class='pre'>title</span> : String (optional)<div class='sub-desc'><p>Title of the message. If not specified, the value of <a href=\"#!/api/gxui.Message-property-Title\" rel=\"gxui.Message-property-Title\" class=\"docClass\">Title</a> property is used.</p>\n</div></li><li><span class='pre'>message</span> : String (optional)<div class='sub-desc'><p>Text of the message. If not specified, the value of <a href=\"#!/api/gxui.Message-property-Message\" rel=\"gxui.Message-property-Message\" class=\"docClass\">Message</a> property is used.</p>\n</div></li><li><span class='pre'>type</span> : String (optional)<div class='sub-desc'><p>Type of the message (see <a href=\"#!/api/gxui.Message-property-Type\" rel=\"gxui.Message-property-Type\" class=\"docClass\">Type</a> property). If not specified, the value of <a href=\"#!/api/gxui.Message-property-Type\" rel=\"gxui.Message-property-Type\" class=\"docClass\">Type</a> property is used.</p>\n</div></li></ul></div></div></div></div></div></div></div>","meta":{}});