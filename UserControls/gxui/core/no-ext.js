/*
 * GxUI Library 2.0.1
 * Copyright (c) 2009, Artech
 * All rights reserved.
 * 
 * GxUI Library is freely distributable under the terms of the BSD license.
 * 
 */

// Fallback mechanism, when Ext is not included

if (!window.Ext) {
	alert("ExtJS not found. To solve the problem, please follow the installation instructions: http://wiki.gxtechnical.com/commwiki/servlet/hwikibypageid?19312.");
}