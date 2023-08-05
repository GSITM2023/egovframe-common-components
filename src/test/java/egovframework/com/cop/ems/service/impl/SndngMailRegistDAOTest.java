package egovframework.com.cop.ems.service.impl;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.exception.FdlException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.impl.EgovFileMngServiceImpl;
import egovframework.com.cmm.service.impl.FileManageDAO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.ems.service.AtchmnFileVO;
import egovframework.com.cop.ems.service.SndngMailVO;
import egovframework.com.test.EgovTestAbstractDAO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 발송메일 등록 DAO 단위 테스트
 *
 * @author 주레피
 *
 */

@ContextConfiguration(classes = { EgovTestAbstractDAO.class, SndngMailRegistDAOTest.class, })

@Configuration

@ImportResource({

        "classpath*:egovframework/spring/com/idgn/context-idgn-MailMsg.xml",

        "classpath*:egovframework/spring/com/idgn/context-idgn-File.xml",
//
//        "classpath*:egovframework/spring/com/idgn/context-idgn-FileSysMntrng.xml",

})

@ComponentScan(

        useDefaultFilters = false,

        basePackages = {

                "egovframework.com.cop.ems.service.impl",

                "egovframework.com.cmm.service.impl",

//                "egovframework.com.cmm.service",

        },

        includeFilters = {

                @Filter(

                        type = FilterType.ASSIGNABLE_TYPE,

                        classes = {

//                                EgovFileMngUtil.class,

                                SndngMailRegistDAO.class,

                                EgovFileMngServiceImpl.class,

                                FileManageDAO.class,

//                                SndngMailDetailDAO.class,

                        }

                )

        }

)

@NoArgsConstructor
@Slf4j
// @Commit
public class SndngMailRegistDAOTest extends EgovTestAbstractDAO {

    /** EgovFileMngService */
    @Autowired
//    @Qualifier("EgovFileMngService")
    private EgovFileMngService egovFileMngService;
//
//    /** EgovFileMngUtil */
//    @Autowired
//    @Qualifier("EgovFileMngUtil")
//    private EgovFileMngUtil fileUtil;
//
    /** File ID Generation */
    @Autowired
    @Qualifier("egovFileIdGnrService")
    private EgovIdGnrService egovFileIdGnrService;

    /** Message ID Generation */
    @Autowired
    @Qualifier("egovMailMsgIdGnrService")
    private EgovIdGnrService egovMailMsgIdGnrService;

    /** SndngMailRegistDAO */
    @Autowired
    private SndngMailRegistDAO sndngMailRegistDAO;

//    /** SndngMailRegistDAO */
//    @Autowired
//    private SndngMailDetailDAO sndngMailDetailDAO;

//    /**
//     * 첨부파일(Mocking) 데이터 생성
//     *
//     */
//    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
//        FileInputStream fileInputStream = new FileInputStream(new File(path));
//        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
//    }

    /**
     * 메일, 첨부파일 데이터 생성
     *
     */
    private void testData(final SndngMailVO sndngMailVO, final LoginVO loginVO) {
        try {
            sndngMailVO.setMssageId(egovMailMsgIdGnrService.getNextStringId());
        } catch (FdlException eFdl) {
            log.error("FdlException egovMailMsgIdGnrService");
        }

        if (loginVO != null) {
            sndngMailVO.setDsptchPerson(loginVO.getId());
        }

        sndngMailVO.setRecptnPerson("dlqorgod@nave.com");
        sndngMailVO.setSj("test 이백행 제목 " + LocalDateTime.now());

        try {
            sndngMailRegistDAO.insertSndngMail(sndngMailVO);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("Exception insertSndngMail testData");
        }
    }

    private void testData(final FileVO fvo) {
        try {
            fvo.setAtchFileId(egovFileIdGnrService.getNextStringId());
        } catch (FdlException e) {
//            e.printStackTrace();
            log.error("FdlException egovFileIdGnrService");
        }
        fvo.setFileSn("0");
        fvo.setFileMg("1");

        String atchFileId = null;
        try {
            atchFileId = egovFileMngService.insertFileInf(fvo);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("Exception insertFileInf");
        }

        if (log.isDebugEnabled()) {
            log.debug("getAtchFileId={}", fvo.getAtchFileId());

            log.debug("atchFileId={}", atchFileId);
        }
    }

    /**
     * 발송메일 발송 테스트
     */
    @Test
    public void testInsertSndngMail() {
        // given
        final SndngMailVO sndngMailVO = new SndngMailVO();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

        try {
            sndngMailVO.setMssageId(egovMailMsgIdGnrService.getNextStringId());
        } catch (FdlException eFdl) {
            log.error("FdlException egovMailMsgIdGnrService");
        }

        if (loginVO != null) {
            sndngMailVO.setDsptchPerson(loginVO.getId());

            if (log.isDebugEnabled()) {
                log.debug("loginVO={}", loginVO);
                log.debug("getId={}", loginVO.getId());
                log.debug("getUniqId={}", loginVO.getUniqId());
            }
        }

        sndngMailVO.setRecptnPerson("dlqorgod@nave.com");
        sndngMailVO.setSj("test 이백행 제목 " + LocalDateTime.now());

        if (log.isDebugEnabled()) {
            log.debug("sndngMailVO={}", sndngMailVO);
            log.debug("getMssageId={}", sndngMailVO.getMssageId());
            log.debug("getDsptchPerson={}", sndngMailVO.getDsptchPerson());
            log.debug("getRecptnPerson={}", sndngMailVO.getRecptnPerson());
            log.debug("getSj={}", sndngMailVO.getSj());
        }

        // when
        int result = 0;
        try {
            result = sndngMailRegistDAO.insertSndngMail(sndngMailVO);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("Exception insertSndngMail");
        }

        // then
        assertEquals(egovMessageSource.getMessage("fail.common.insert"), 1, result);
    }

    /**
     * 발송메일의 첨부파일 리스트 목록 조회 테스트
     */
    @Test
    public void testSelectAtchmnFileList() {
        // given
        final FileVO fvo = new FileVO();
        testData(fvo);

        final SndngMailVO sndngMailVO = new SndngMailVO();
        sndngMailVO.setAtchFileId(fvo.getAtchFileId());

        // when
        List<AtchmnFileVO> resultList = null;
        try {
            resultList = sndngMailRegistDAO.selectAtchmnFileList(sndngMailVO);
        } catch (Exception e) {
            log.error("Exception selectAtchmnFileList");
        }

        // then
        assertSelectAtchmnFileList(sndngMailVO, resultList);
    }

    private void assertSelectAtchmnFileList(final SndngMailVO sndngMailVO, final List<AtchmnFileVO> resultList) {
        if (log.isDebugEnabled()) {
            for (final AtchmnFileVO result : resultList) {
                log.debug("result={}", result);
                log.debug("atchFileId={}", result.getAtchFileId());
            }
        }

        assertSelectAtchmnFileList(sndngMailVO, resultList.get(0));
    }

    private void assertSelectAtchmnFileList(final SndngMailVO sndngMailVO, final AtchmnFileVO result) {
        if (log.isDebugEnabled()) {
            log.debug("getAtchFileId={}, {}", sndngMailVO.getAtchFileId(), result.getAtchFileId());
        }

        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), sndngMailVO.getAtchFileId(),
                result.getAtchFileId());
    }

    /**
     * 발송메일의 발송상태 업데이트 테스트
     */
    @Test
    public void testUpdateSndngMail() {
        // given
        final SndngMailVO sndngMailVO = new SndngMailVO();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        testData(sndngMailVO, loginVO);

        // COM024 발송결과구분
        // C 완료
        // F 실패
        // R 요청
        sndngMailVO.setSndngResultCode("C");
//        sndngMailVO.setSndngResultCode("F");
//        sndngMailVO.setSndngResultCode("R");

        // when
        int result = 0;
        try {
            result = sndngMailRegistDAO.updateSndngMail(sndngMailVO);
        } catch (Exception e) {
            log.error("Exception updateSndngMail");
        }

        // then
        assertEquals(egovMessageSource.getMessage("fail.common.update"), 1, result);
    }

}
