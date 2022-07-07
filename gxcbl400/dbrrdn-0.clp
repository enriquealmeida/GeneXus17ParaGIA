/*-------------------------------------------------------------------*/
/* DATA BASE REORGANIZATION: COMPILE REDUNDANCY PROGRAMS             */
/*-------------------------------------------------------------------*/
 DBRRDN:     PGM        PARM(&PGMLIB &DTALIB &JOBTYPE)
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
             CHKOBJ     OBJ(&PGMLIB/DBRRDN) OBJTYPE(*DTAARA)
             MONMSG     MSGID(CPF9801) +
                        EXEC(CRTDTAARA DTAARA(&PGMLIB/DBRRDN) +
                                       TYPE(*CHAR) +
                                       LEN(11) +
                                       TEXT('Data Base +
                                             Redundancy Programs Creation'))
/*-------------------------------------------------------------------*/
             RTVDTAARA  DTAARA(&PGMLIB/DBRRDN (11 1)) RTNVAR(&OK)
             IF         COND(&OK *EQ 'X') THEN(DO)
                 RTVDTAARA DTAARA(&PGMLIB/DBRRDN (1 10)) +
                           RTNVAR(&CMDLBL)
                 IF         COND(&CMDLBL *EQ '#1') + \
                            THEN(GOTO CMDLBL(#1))
             ENDDO
/*-------------------------------------------------------------------*/
 #1:            \
    CHGDTAARA  DTAARA(&PGMLIB/DBRRDN (1 11)) + \
               VALUE('#2')                     \
    SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +       \
               MSGDTA('Creating redundancy programs: Creating program + \
                       #1') +  \
               TOPGMQ(*EXT) MSGTYPE(*STATUS)        \
    CALL       PGM(GX/GXCGCL) PARM(#3 &PGMLIB &DTALIB #1  + \
                        *EXEC *DLT) \

/*-------------------------------------------------------------------*/ 
             RCLRSC
/*-------------------------------------------------------------------*/

/*-------------------------------------------------------------------*/
 DBRCOMPL:
             CHGDTAARA  DTAARA(&PGMLIB/DBRRDN (1 11)) +
                        VALUE('           ')
/*-------------------------------------------------------------------*/
/* NORMAL END                                                        */
/*-------------------------------------------------------------------*/
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
