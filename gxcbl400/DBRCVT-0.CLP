/*-------------------------------------------------------------------*/
/* DATA BASE CONVERTION                                              */
/*-------------------------------------------------------------------*/
 DBRCVT:     PGM        PARM(&PGMLIB &DTALIB &JOBTYPE)
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
/* Standard error-handling monitoring                                */
/*-------------------------------------------------------------------*/
             MONMSG     MSGID(CPF0000 GXM0000) +
                        EXEC(GOTO CMDLBL(STDERR1))
/*-------------------------------------------------------------------*/
        CHGCURLIB CURLIB(&DTALIB)
/*-------------------------------------------------------------------*/
/* Object Allocation                                                 */
/*-------------------------------------------------------------------*/
        ALCOBJ    OBJ((&DTALIB/#2 *FILE *EXCL))
/*-------------------------------------------------------------------*/
 CHKOBJ (&DTALIB/#1) OBJTYPE(*FILE)\
 MONMSG MSGID(CPF9801) EXEC(GOTO CMDLBL(#1))\
 #5/GXDLTF FILE(&DTALIB/#1)\
#1:\
 SNDPGMMSG MSGID(CPF9898) MSGF(QCPFMSG) +\
 MSGDTA('Creating Temporary Tables: Creating table #1') +\
 TOPGMQ(*EXT) MSGTYPE(*STATUS)\
 CRTPF &DTALIB/#1 &PGMLIB/QDDSSRC +\
 SHARE(*NO) AUT(*LIBCRTAUT) OPTION(*NOLIST *NOSRC) SIZE(*NOMAX)\
 SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +\
 MSGDTA('Copying Information: Converting #2 To #1') +\
 TOPGMQ(*EXT) MSGTYPE(*STATUS) \
 CPYF &DTALIB/#2 &DTALIB/#1 FMTOPT(*MAP) MBROPT(*REPLACE)\
 MONMSG MSGID(CPF2817) EXEC(DO)\
 RCVMSG MSGTYPE(*DIAG) RMV(*NO) MSG(&MSG) MSGID(&MSGID)\
 IF (&MSGID *NE 'CPF2869') GOTO STDERR1\
 ENDDO\
 #5/GXDLTF FILE(&DTALIB/#2)\
 SNDPGMMSG MSGID(CPF9898) MSGF(QCPFMSG) +\
 MSGDTA('Renaming Tables: Renaming Table #1 To #2') +\
 TOPGMQ(*EXT) MSGTYPE(*STATUS)\
 CALL PGM(#5/GXRENTBL) PARM(&DTALIB '#1' '#2')    
 CPYSRCF FROMFILE(&PGMLIB/QDDSSRC) TOFILE(&PGMLIB/QDDSSRC) + \
  FROMMBR(#1) TOMBR(#2) MBROPT(*REPLACE) SRCOPT(*SAME) \
 MONMSG MSGID(CPF0000) \



/* START JOURNAL FOR REORGANIZED FILES */

    CHKOBJ (&DTALIB/QSQJRN) OBJTYPE(*JRN) 
    MONMSG MSGID(CPF9801) EXEC(GOTO CMDLBL(NOJRN)) 


     STRJRNPF FILE(&DTALIB/#3) JRN(&DTALIB/QSQJRN) + \
              IMAGES(*BOTH)                          \
     MONMSG MSGID(CPF7030)   

NOJRN:


/* CREATE LOGICAL FILES */

        CHKOBJ (&DTALIB/#4) OBJTYPE(*FILE)          \
        MONMSG MSGID(CPF9801) EXEC(GOTO CMDLBL(#4))  \
        #5/GXDLTF FILE(&DTALIB/#4)                       \
 #4: SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) +    \
          MSGDTA('Creating Indexes: Creating index #4') +     \
          TOPGMQ(*EXT) MSGTYPE(*STATUS)     \
        CRTLF FILE(&DTALIB/#4) SRCFILE(&PGMLIB/QDDSSRC) + \
          SHARE(*NO) AUT(*LIBCRTAUT) OPTION(*NOLIST *NOSRC)


/*-------------------------------------------------------------------*/ 
             RCLRSC
/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
/* Object Deallocation                                               */
/*-------------------------------------------------------------------*/
        DLCOBJ    OBJ((&DTALIB/#2 *FILE *EXCL))
/*-------------------------------------------------------------------*/
 DBRCOMPL:
/*-------------------------------------------------------------------*/
/* NORMAL END                                                        */
/*-------------------------------------------------------------------*/
             IF         COND(&JOBTYPE *EQ '*BATCH') +
                        THEN(SNDPGMMSG MSGID(GXM9000) +
                                       MSGF(#5/GXMSGF) +
                                       MSGDTA('Data Base +
                                               Reorganization +
                                               completed +
                                               successfully.') +
                                       MSGTYPE(*COMP))
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
