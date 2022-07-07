/*-------------------------------------------------------------------*/
/* DATA BASE REORGANIZATION: CREATE PROGRAMS                         */
/*-------------------------------------------------------------------*/
 DBRPGM:     PGM        PARM(&PGMLIB &DTALIB &JOBTYPE)
/*-------------------------------------------------------------------*/
/* PARAMETERS: &PGMLIB  (PROGRAMS LIBRARY)                           */
/*             &JOBTYPE (*INTER/*BATCH)                                */
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
             DCL        VAR(&MSGID)   TYPE(*CHAR) LEN(7)
             DCL        VAR(&MSGDTA)  TYPE(*CHAR) LEN(79)
             DCL        VAR(&MSGF)    TYPE(*CHAR) LEN(10)
             DCL        VAR(&MSGFLIB) TYPE(*CHAR) LEN(10)
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
             CHKOBJ     OBJ(&PGMLIB/DBRPGM) OBJTYPE(*DTAARA)
             MONMSG     MSGID(CPF9801) +
                        EXEC(CRTDTAARA DTAARA(&PGMLIB/DBRPGM) +
                                       TYPE(*CHAR) +
                                       LEN(11) +
                                       TEXT('Data Base Reorganization +
                                             Create Programs'))
/*-------------------------------------------------------------------*/
             RTVDTAARA  DTAARA(&PGMLIB/DBRPGM (1 1)) RTNVAR(&OK)
             IF         COND(&OK *EQ 'X') THEN(DO)
                 RTVDTAARA DTAARA(&PGMLIB/DBRPGM (2 10)) +
                           RTNVAR(&CMDLBL)
                 IF         COND(&CMDLBL *EQ 'GXSTDPGMS') + \
                            THEN(GOTO CMDLBL(GXSTDPGMS))
                 IF         COND(&CMDLBL *EQ '#1') + \
                            THEN(GOTO CMDLBL(#1))
             ENDDO
/*-------------------------------------------------------------------*/
 GXSTDPGMS:
             CHGDTAARA  DTAARA(&PGMLIB/DBRPGM (1 11)) +
                        VALUE('XGXSTDPGMS')
             SNDPGMMSG  MSGID(CPF9898) + 
                        MSGF(QCPFMSG) + 
                        MSGDTA('Creating convertion programs: +
                        Creating standard programs') +
                        TOPGMQ(*EXT) +
                        MSGTYPE(*STATUS)

             /* GX library must be added to find commands used by */
             /* the DBRDLTF command                               */
             ADDLIBLE   #3
             MONMSG     CPF2103

             CRTCLPGM   PGM(&PGMLIB/DBRDLTF) +
                        SRCFILE(&PGMLIB/QCLSRC) +
                        TGTRLS(#4)
 #1:                                                               \
             CHGDTAARA  DTAARA(&PGMLIB/DBRPGM (1 11)) +            \
                        VALUE('X#1')                                \
             SNDPGMMSG  MSGID(CPF9898) +                  \
                   MSGF(QCPFMSG) +                   \
                   MSGDTA('Creating convertion programs: + \
                   Creating program for converting table #2') + \
                   TOPGMQ(*EXT) +  \
                   MSGTYPE(*STATUS) \
             CALL       PGM(#3/GXCGCL)  +  \
                        PARM(#3 &PGMLIB &DTALIB #1 *EXEC *DLT) 
/*-------------------------------------------------------------------*/
/* NORMAL END                                                        */
/*-------------------------------------------------------------------*/
 DBRPGMEND:
             CHGDTAARA  DTAARA(&PGMLIB/DBRPGM (1 11)) +
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

