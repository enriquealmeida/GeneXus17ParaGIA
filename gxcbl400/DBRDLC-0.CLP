/*-------------------------------------------------------------------*/
/* DATA BASE REORGANIZATION: DEALOCATE TABLES                        */
/*-------------------------------------------------------------------*/
 DBRALC:     PGM        PARM(&PGMLIB &DTALIB)

             DCL        VAR(&DTALIB) TYPE(*CHAR) LEN(10)
             DCL        VAR(&PGMLIB) TYPE(*CHAR) LEN(10)
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
/* UNLOCK TABLES                                                     */
/*-------------------------------------------------------------------*/

   SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) + \
              MSGDTA('Deallocating: Table #1') + \
              TOPGMQ(*EXT) MSGTYPE(*STATUS) \
   DLCOBJ     OBJ((&DTALIB/#1 *FILE *EXCL)) \
   MONMSG     MSGID(CPF0000) EXEC(DO) \
              RCVMSG MSGTYPE(*EXCP) RMV(*NO) MSGID(&MSGID) \
              SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) + \
                         MSGDTA('* Could not deallocate file #1') + \
                         TOPGMQ(*EXT) MSGTYPE(*STATUS)     \
              GOTO ABREND \
              ENDDO \

/*-------------------------------------------------------------------*/
/* NEW TABLES, JUST IN CASE                                          */
/*-------------------------------------------------------------------*/

   SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) + \
              MSGDTA('Deallocating: Table #2') + \
              TOPGMQ(*EXT) MSGTYPE(*STATUS) \
   DLCOBJ     OBJ((&DTALIB/#2 *FILE *EXCL)) \
   MONMSG     MSGID(CPF0000) EXEC(DO) \
              RCVMSG MSGTYPE(*EXCP) RMV(*NO) MSGID(&MSGID) \
              IF (&MSGID = 'CPF9801') THEN(GOTO #2) \
              IF (&MSGID = 'CPF1005') THEN(GOTO #2) \
              SNDPGMMSG  MSGID(CPF9898) MSGF(QCPFMSG) + \
                         MSGDTA('* Could not deallocate file #2') + \
                         TOPGMQ(*EXT) MSGTYPE(*STATUS)     \
              GOTO ABREND \
              ENDDO \
#2:

         RETURN
 ABREND:

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

             IF         COND(&MSGID *EQ '       ') THEN( +
                 SNDPGMMSG  MSGID(&MSGID) +
                           MSGF(&MSGFLIB/&MSGF) +
                           MSGDTA(&MSGDTA) +
                           MSGTYPE(*ESCAPE) )
/*-------------------------------------------------------------------*/
/* END OF PROGRAM                                                    */
/*-------------------------------------------------------------------*/
             ENDPGM

