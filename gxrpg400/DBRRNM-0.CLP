/*-------------------------------------------------------------------*/
/* DATA BASE REORGANIZATION: RENAME FILES                            */
/*-------------------------------------------------------------------*/
 DBRRNM:     PGM        PARM(&PGMLIB &DTALIB)
/*-------------------------------------------------------------------*/
/* PARAMETERS: &PGMLIB  (PROGRAMS LIBRARY)                           */
/*             &DTALIB  (DATA LIBRARY)                               */
/*-------------------------------------------------------------------*/
             DCL        VAR(&PGMLIB) TYPE(*CHAR) LEN(10)
             DCL        VAR(&DTALIB) TYPE(*CHAR) LEN(10)
/*-------------------------------------------------------------------*/
/* OTHER VARIABLES                                                   */
/*-------------------------------------------------------------------*/
             DCL        VAR(&CMDLBL) TYPE(*CHAR) LEN(10)
             DCL        VAR(&OK) TYPE(*CHAR) LEN(1)
/*-------------------------------------------------------------------*/
/* Standard error-handling variables                                 */
/*-------------------------------------------------------------------*/
             DCL        VAR(&ERRORSW) TYPE(*LGL)
             DCL        VAR(&MSGID)   TYPE(*CHAR) LEN(7)
             DCL        VAR(&MSGDTA)  TYPE(*CHAR) LEN(79)
             DCL        VAR(&MSGF)    TYPE(*CHAR) LEN(10)
             DCL        VAR(&MSGFLIB) TYPE(*CHAR) LEN(10)
/*-------------------------------------------------------------------*/
/* Standard error-handling monitoring                                */
/*-------------------------------------------------------------------*/
             MONMSG     MSGID(CPF0000 GXM0000) +
                        EXEC(GOTO CMDLBL(STDERR1))

             CHKOBJ     OBJ(&PGMLIB/DBRRNM) OBJTYPE(*DTAARA)
             MONMSG     MSGID(CPF9801) +
                        EXEC(CRTDTAARA DTAARA(&PGMLIB/DBRRNM) +
                                       TYPE(*CHAR) +
                                       LEN(11) +
                                       TEXT('Data Base Reorganization +
                                             Rename tables step'))

             RTVDTAARA  DTAARA(&PGMLIB/DBRRNM (1 1)) RTNVAR(&OK)
             IF  COND(&OK *EQ 'X') THEN(DO)
                 RTVDTAARA DTAARA(&PGMLIB/DBRRNM (2 10)) +
                           RTNVAR(&CMDLBL)
                 IF COND(&CMDLBL *EQ '#2') THEN(GOTO CMDLBL(#2))
             ENDDO

/*-------------------------------------------------------------------*/
/* RENAME TABLES                                                     */
/*-------------------------------------------------------------------*/
#2: CALL       PGM(&PGMLIB/DBRDLTF) PARM(&DTALIB '#2') \
    CHGDTAARA  DTAARA(&PGMLIB/DBRRNM (1 11)) VALUE('X#2') \
    SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +    \
      MSGDTA('Renaming tables: Creating table #2') + \
      TOPGMQ(*EXT) MSGTYPE(*STATUS)     \
    CRTPF FILE(&DTALIB/#2) SRCFILE(&PGMLIB/QDDSSRC) + \
      SHARE(*NO) AUT(*LIBCRTAUT) OPTION(*NOLIST *NOSRC) SIZE(#7)\
    SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +       \
      MSGDTA('Renaming tables: Copying #1 To #2') +  \
     TOPGMQ(*EXT) MSGTYPE(*STATUS) \
    CPYF FROMFILE(#1) TOFILE(#2) FMTOPT(*MAP) MBROPT(*REPLACE)\
    MONMSG MSGID(CPF2817) EXEC(DO) \
     RCVMSG MSGTYPE(*DIAG) RMV(*NO) MSGID(&MSGID) \
     IF (&MSGID *NE 'CPF2869') GOTO STDERR1  \
   ENDDO \
   #6/GXDLTF FILE(&DTALIB/#1)                      

/*-------------------------------------------------------------------*/
/* RENAME TEMPORARY FILES                                            */
/*-------------------------------------------------------------------*/

             CHGDTAARA  DTAARA(&PGMLIB/DBREXC (1 11)) +
                        VALUE('XGXRENTBL')

             SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) + \
MSGDTA('Renaming temporary tables: Renaming Table #2 To #3') + \
                        TOPGMQ(*EXT) + \
                        MSGTYPE(*STATUS) \
             CALL       PGM(GXRENTBL) PARM(&DTALIB '#2' '#3') 

             SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +       \
MSGDTA('Renaming temporary tables: Renaming Table #4 To #5') + \
                        TOPGMQ(*EXT) + \
                        MSGTYPE(*STATUS) \
             CALL       PGM(GXRENTBL) PARM(&DTALIB '#4' '#5') \
             CPYSRCF    FROMFILE(&PGMLIB/QDDSSRC) + \
                        TOFILE(&PGMLIB/QDDSSRC) + \
                        FROMMBR(#4) + \
                        TOMBR(#5) + \
                        MBROPT(*REPLACE) + \
                        SRCOPT(*SAME) \
             MONMSG     MSGID(CPF0000) \

/*-------------------------------------------------------------------*/
/* NORMAL END                                                        */
/*-------------------------------------------------------------------*/
             CHGDTAARA  DTAARA(&PGMLIB/DBRRNM (1 11)) +
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
