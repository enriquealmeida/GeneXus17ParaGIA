/*-------------------------------------------------------------------*/
/* DATA BASE REORGANIZATION: EXECUTE PROGRAMS                        */
/*-------------------------------------------------------------------*/
 DBREXC:     PGM        PARM(&PGMLIB &DTALIB &JOBTYPE)
/*-------------------------------------------------------------------*/
/* PARAMETERS: &PGMLIB  (PROGRAMS LIBRARY)                           */
/*             &DTALIB  (DATA LIBRARY)                               */
/*             &JOBTYPE (*INTER/*BATCH)                              */
/*-------------------------------------------------------------------*/
             DCL        VAR(&PGMLIB) TYPE(*CHAR) LEN(10)
             DCL        VAR(&DTALIB) TYPE(*CHAR) LEN(10)
             DCL        VAR(&JOBTYPE) TYPE(*CHAR) LEN(6)
/*-------------------------------------------------------------------*/
/* OTHER VARIABLES                                                   */
/*-------------------------------------------------------------------*/
             DCL        VAR(&CMDLBL) TYPE(*CHAR) LEN(10)
             DCL        VAR(&OK) TYPE(*CHAR) LEN(1)
/*-------------------------------------------------------------------*/
/* Standard error-handling variables                                 */
/*-------------------------------------------------------------------*/
             DCL        VAR(&ERRORSW) TYPE(*LGL)
             DCL        VAR(&MSG)     TYPE(*CHAR) LEN(200)
             DCL        VAR(&MSGID)   TYPE(*CHAR) LEN(7)
             DCL        VAR(&MSGDTA)  TYPE(*CHAR) LEN(79)
             DCL        VAR(&MSGF)    TYPE(*CHAR) LEN(10)
             DCL        VAR(&MSGFLIB) TYPE(*CHAR) LEN(10)
/*-------------------------------------------------------------------*/
/* STATUS DISPLAY FILE                                               */
/*-------------------------------------------------------------------*/
/*             DCLF       FILE(GXEXCINF)*/
/*-------------------------------------------------------------------*/
/* Standard error-handling monitoring                                */
/*-------------------------------------------------------------------*/
             MONMSG     MSGID(CPF0000 GXM0000) +
                        EXEC(GOTO CMDLBL(STDERR1))
/*-------------------------------------------------------------------*/
/* CHECK-POINT DATA-AREA FORMAT:                                     */
/*                                                                   */
/*      1 TO 10 - STEP ID.                                           */
/*     11 TO 11 - STEP COMPLETION CODE (N=NO/Y=YES).                 */
/*-------------------------------------------------------------------*/
             CHKOBJ     OBJ(&PGMLIB/DBREXC) OBJTYPE(*DTAARA)
             MONMSG     MSGID(CPF9801) +
                        EXEC(CRTDTAARA DTAARA(&PGMLIB/DBREXC) +
                                       TYPE(*CHAR) +
                                       LEN(11) +
                                       TEXT('Data Base Reorganization +
                                             Programs Execution'))
/*-------------------------------------------------------------------*/
             ADDLIBLE   &DTALIB
             MONMSG     CPF2103
             ADDLIBLE   &PGMLIB
             MONMSG     CPF2103
             ADDLIBLE   GX
             MONMSG     CPF2103

             RTVDTAARA  DTAARA(&PGMLIB/DBREXC (1 1)) RTNVAR(&OK)
   IF         COND(&OK *EQ 'X') THEN(DO)
      RTVDTAARA DTAARA(&PGMLIB/DBREXC (2 10)) +
                RTNVAR(&CMDLBL)
      IF COND(&CMDLBL *EQ '#1') THEN(GOTO CMDLBL(#1))
      IF COND(&CMDLBL *EQ '#5') THEN(GOTO CMDLBL(#5))
      IF COND(&CMDLBL *EQ 'GXNEWFLS') THEN(GOTO CMDLBL(GXNEWFLS))
      IF COND(&CMDLBL *EQ 'GXJOURNAL') THEN(GOTO CMDLBL(GXJOURNAL))
      IF COND(&CMDLBL *EQ 'GXCFILES  ') THEN(GOTO CMDLBL(GXCFILES))
      IF COND(&CMDLBL *NE 'DBEF#7') THEN(GOTO CMDLBL(DBEF#7))
   ENDDO
/*-------------------------------------------------------------------*/ 
/* EXECUTE REORGANIZATION PROGRAMS                                   */ 
/*-------------------------------------------------------------------*/
 #1:            \
             CHGDTAARA  DTAARA(&PGMLIB/DBREXC (1 11)) + \
                        VALUE('X#1')                     \
             CLRPFM     FILE(&DTALIB/#3) MBR(*FIRST) \
             SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +       \
                        MSGDTA('Reorganizing data: Executing + \
                        convertion for #1') +  \
                        TOPGMQ(*EXT) MSGTYPE(*STATUS)        \
             CALL       PGM(#1)                         

             RCLRSC

/*-------------------------------------------------------------------*/ 
/* RENAME FILES                                                      */ 
/*-------------------------------------------------------------------*/
             CHGDTAARA  DTAARA(&PGMLIB/DBREXC (1 11)) +
                        VALUE('XGXCFILES')

 GXCFILES:
             CALL       PGM(&PGMLIB/DBRRNM) PARM(&PGMLIB &DTALIB)

/*-------------------------------------------------------------------*/ 
/* CREATE NEW PHYSICAL FILES                                         */ 
/*-------------------------------------------------------------------*/

             CHGDTAARA  DTAARA(&PGMLIB/DBREXC (1 11)) +
                        VALUE('XGXNEWFLS')
 GXNEWFLS:
             CALL       PGM(&PGMLIB/DBRNTB) PARM(&PGMLIB &DTALIB)
             
/*-------------------------------------------------------------------*/ 
/* START JOURNAL FOR REORGANIZED FILES                               */ 
/*-------------------------------------------------------------------*/

             CHGDTAARA  DTAARA(&PGMLIB/DBREXC (1 11)) +
                        VALUE('XGXJOURNAL')
 GXJOURNAL:
             CALL       PGM(&PGMLIB/DBRJRN) PARM(&PGMLIB &DTALIB)

/*-------------------------------------------------------------------*/ 
/* DELETE ABOLISHED LOGICAL FILES                                    */ 
/*-------------------------------------------------------------------*/
             DLTF       FILE(&DTALIB/#4) \
             MONMSG     MSGID(CPF2105) \

/*-------------------------------------------------------------------*/ 
/* CREATE FILE VIEW LOGICAL FILES                                    */ 
/*-------------------------------------------------------------------*/
 #5: \
             CALL       PGM(&PGMLIB/DBRDLTF) PARM(&DTALIB '#5') \
             CHGDTAARA  DTAARA(&PGMLIB/DBREXC (1 11)) + \
                        VALUE('X#5') \
             SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +    \
                        MSGDTA('Creating file views: #5') + \
                        TOPGMQ(*EXT) + \
                         MSGTYPE(*STATUS) \
             CRTLF      FILE(&DTALIB/#5) SRCFILE(&PGMLIB/QDDSSRC) + \
                        SHARE(*NO) AUT(*LIBCRTAUT) OPTION(*NOLIST *NOSRC)
             MONMSG     MSGID(CPF0000) EXEC(DO) \
                        SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +    \
                        MSGDTA('Could not create file view #5') + \
                        TOPGMQ(*EXT) MSGTYPE(*STATUS) \
                        ENDDO 


/*-------------------------------------------------------------------*/
/* NORMAL END - DO NEXTFILE                                          */
/*-------------------------------------------------------------------*/
 DBEF#7: \
            CHGDTAARA DTAARA(&PGMLIB/DBREXC (1 11)) VALUE('XDBEF#6') \
            CALL  PGM(&PGMLIB/#6)  PARM(&PGMLIB &DTALIB &JOBTYPE)
/*-------------------------------------------------------------------*/
/* NORMAL END                                                        */
/*-------------------------------------------------------------------*/
 DBRCOMPL:
             CHGDTAARA  DTAARA(&PGMLIB/DBREXC (1 11)) +
                        VALUE('           ')
             RETURN

/*-------------------------------------------------------------------*/
/* Standard error-handling routine                                   */
/*-------------------------------------------------------------------*/
 STDERR1:
             IF         COND(&ERRORSW) +
                        THEN(SNDPGMMSG MSGID(CPF9999) +
                                       MSGF(QCPFMSG) +
                                       MSGTYPE(*ESCAPE))

             CHGVAR     VAR(&ERRORSW) +
                        VALUE('1')
 STDERR2:
             RCVMSG     MSGTYPE(*DIAG) +
                        MSGDTA(&MSGDTA) +
                        MSGID(&MSGID) +
                        MSGF(&MSGF) +
                        MSGFLIB(&MSGFLIB)

             IF         COND(&MSGID *EQ '       ') +
                        THEN(GOTO CMDLBL(STDERR3))

             SNDPGMMSG  MSGID(&MSGID) +
                        MSGF(&MSGFLIB/&MSGF) +
                        MSGDTA(&MSGDTA) +
                        MSGTYPE(*DIAG)

             GOTO       CMDLBL(STDERR2)
 STDERR3:
             RCVMSG     MSGTYPE(*EXCP) +
                        MSGDTA(&MSGDTA) +
                        MSGID(&MSGID) +
                        MSGF(&MSGF) +
                        MSGFLIB(&MSGFLIB)

             SNDPGMMSG  MSGID(&MSGID) +
                        MSGF(&MSGFLIB/&MSGF) +
                        MSGDTA(&MSGDTA) +
                        MSGTYPE(*ESCAPE)
/*-------------------------------------------------------------------*/
/* END OF PROGRAM                                                    */
/*-------------------------------------------------------------------*/
             ENDPGM
