package egovframework.com.cop.ems.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.ems.service.SndngMailVO;
import egovframework.com.test.EgovTestAbstractDAO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 발송메일 내역 조회 DAO 단위 테스트
 *
 * @author 주레피
 *
 */

@ContextConfiguration(classes = { EgovTestAbstractDAO.class, SndngMailDtlsDAOTest.class, })

@Configuration

@ImportResource({

//        "classpath*:egovframework/spring/com/idgn/context-idgn-MailMsg.xml",
//
//        "classpath*:egovframework/spring/com/idgn/context-idgn-File.xml",
//
//        "classpath*:egovframework/spring/com/idgn/context-idgn-FileSysMntrng.xml",

})

@ComponentScan(

        useDefaultFilters = false,

        basePackages = {

                "egovframework.com.cop.ems.service.impl",

//                "egovframework.com.cmm.service",

        },

        includeFilters = {

                @Filter(

                        type = FilterType.ASSIGNABLE_TYPE,

                        classes = {

//                                EgovFileMngServiceImpl.class,
//
//                                EgovFileMngUtil.class,
//
//                                FileManageDAO.class,
//

                                SndngMailDtlsDAO.class,

                                SndngMailRegistDAO.class,

                        }

                )

        }

)

@NoArgsConstructor
@Slf4j
// @Commit
public class SndngMailDtlsDAOTest extends EgovTestAbstractDAO {
//    /** EgovFileMngService */
//    @Autowired
//    @Qualifier("EgovFileMngService")
//    private EgovFileMngService fileMngService;
//
//    /** EgovFileMngUtil */
//    @Autowired
//    @Qualifier("EgovFileMngUtil")
//    private EgovFileMngUtil fileUtil;
//
//    /** File ID Generation */
//    @Autowired
//    @Qualifier("egovFileIdGnrService")
//    private EgovIdGnrService egovFileIdGnrService;
//
//    /** Message ID Generation */
//    @Autowired
//    @Qualifier("egovMailMsgIdGnrService")
//    private EgovIdGnrService egovMailMsgIdGnrService;

    /** SndngMailRegistDAO */
    @Autowired
    private SndngMailRegistDAO sndngMailRegistDAO;

    /** SndngMailRegistDAO */
    @Autowired
    private SndngMailDtlsDAO sndngMailDtlsDAO;

//    /**
//     * 첨부파일(Mocking) 데이터 생성
//     *
//     */
//    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path)
//            throws IOException {
//        FileInputStream fileInputStream = new FileInputStream(new File(path));
//        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
//    }

    /**
     * SndngMailVO
     */
    private SndngMailVO testDataSndngMailVO;

    /**
     * LoginVO
     */
    private LoginVO loginVO;

    @Before
    @Override
    public void setUp() {
        super.setUp();

        loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

        testDataSndngMailVO = new SndngMailVO();

        testDataSndngMailVO.setMssageId("TEST_000000000000001");

        if (loginVO != null) {
            testDataSndngMailVO.setDsptchPerson(loginVO.getId());
        }

        testDataSndngMailVO.setRecptnPerson("egovframesupport@gmail.com");

        testDataSndngMailVO.setSj("test 이백행 제목 " + LocalDateTime.now());

        try {
            sndngMailRegistDAO.insertSndngMail(testDataSndngMailVO);
        } catch (DataAccessException e) {
//            e.printStackTrace();
            log.error("Exception insertSndngMail");
            error(e);
            fail("Exception insertSndngMail");
        }
    }

//    /**
//     * 메일, 첨부파일 데이터 생성
//     *
//     */
//    private void testData(final SndngMailVO sndngMailVO, final LoginVO loginVO) {
//        // 메시지ID 설정
//        String mssageId = "";
//        try {
//            mssageId = egovMailMsgIdGnrService.getNextStringId();
//        } catch (FdlException eFdl) {
//            log.error("FdlException egovMailMsgIdGnrService");
//            fail("FdlException egovMailMsgIdGnrService");
//        }
//        sndngMailVO.setMssageId(mssageId);
//        /**
//         * 발송결과코드(CDK-COM-024) 설정 R 요청 F 실패 C 완료
//         **/
//        if (StringUtils.defaultIfBlank(sndngMailVO.getSndngResultCode(), "").isEmpty()) {
//            sndngMailVO.setSndngResultCode("C");
//        }
//        sndngMailVO.setSj("[테스트메일] 단위 테스트"); // 제목 설정
//
//        List<FileVO> _result = new ArrayList<FileVO>();
//        String _atchFileId = "";
//        MockMultipartFile mockMultipartFile = null;
//        try {
//            // 이미 존재하는 sample.png 파일로 첨부파일 mocking
//            mockMultipartFile = getMockMultipartFile("sample.png", "png",
//                    "src/test/resources/egovframework/data/sample.png");
//        } catch (IOException eIO) {
//            log.error("IOException MultipartFile create");
//            fail("IOException MultipartFile create");
//        }
//
//        final Map<String, MultipartFile> files = new HashMap<String, MultipartFile>();
//        files.put("sample.png", mockMultipartFile);
//
//        // 첨부파일 생성
//        try {
//            _result = fileUtil.parseFileInf(files, "MSG_", 0, "", "");
//            _atchFileId = fileMngService.insertFileInfs(_result);
//        } catch (Exception e) {
//            log.error("Exception Mail attach file insert");
//            fail("Exception test sndngMailVO attach file insert");
//        } // 파일이 생성되고나면 생성된 첨부파일 ID를 리턴한다.
//
//        String orignlFileList = "";
//        for (int i = 0; i < _result.size(); i++) {
//            FileVO fileVO = _result.get(i);
//            orignlFileList = fileVO.getOrignlFileNm();
//        }
//
//        // 발신자 설정
//        sndngMailVO.setDsptchPerson(loginVO == null ? "" : EgovStringUtil.isNullToString(loginVO.getId()));
//        // 수신자 설정
//        sndngMailVO.setRecptnPerson("open.egovframe@gmail.com"); // 수신자 설정
//        sndngMailVO.setAtchFileId(_atchFileId); // 첨부파일ID 설정
//        // 첨부파일명 설정
//        sndngMailVO.setOrignlFileNm(orignlFileList);
//
//        // 발송메일을 등록
//        try {
//            sndngMailRegistDAO.insertSndngMail(sndngMailVO);
//        } catch (Exception e) {
//            log.error("Exception test sndngMailVO insert");
//            fail("Exception test sndngMailVO insert");
//        }
//    }

    /**
     * 발송메일 목록 조회 테스트
     */
    @Test
    public void testSelectSndngMailList() {
//        // given
//        final SndngMailVO sndngMailVO = new SndngMailVO();
//        sndngMailVO.setSndngResultCode("C"); // 발송결과코드를 '요청'으로 설정
//        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//        testData(sndngMailVO, loginVO);
//
//        // when
        final ComDefaultVO comDefaultVO = new ComDefaultVO();
        comDefaultVO.setFirstIndex(0);
        comDefaultVO.setRecordCountPerPage(1);

        comDefaultVO.setSearchCondition("1");
        comDefaultVO.setSearchKeyword(testDataSndngMailVO.getSj());

        List<SndngMailVO> resultList = null;
        try {
            resultList = sndngMailDtlsDAO.selectSndngMailList(comDefaultVO);
        } catch (DataAccessException e) {
            log.error("Exception selectSndngMailList");
            error(e);
            fail("Exception selectSndngMailList");
        }

        // then
        assert1(resultList);
    }

    private void assert1(final List<SndngMailVO> resultList) {
//        assertTrue(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 0 < resultList.size());
//
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), sndngMailVO.getMssageId(),
//                resultList.get(0).getMssageId());
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), "C", sndngMailVO.getSndngResultCode());
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), "완료", resultList.get(0).getSndngResultCode());

        for (SndngMailVO result : resultList) {
            if (log.isDebugEnabled()) {
                log.debug("sndngMailVO, result = {}, {}", testDataSndngMailVO, result);
                log.debug("getMssageId = {}, {}", testDataSndngMailVO.getMssageId(), result.getMssageId());
                log.debug("getSndngResultCode = {}, {}", testDataSndngMailVO.getSndngResultCode(), result.getSndngResultCode());
            }
        }

        assert1(resultList.get(0));
    }

    private void assert1(final SndngMailVO result) {
        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), testDataSndngMailVO.getMssageId(),
                result.getMssageId());
    }

//    /**
//     * 발송메일 총건수 조회 테스트
//     */
//
//    @Test
//    public void testSelectSndngMailListTotCnt() {
//        // given
//        final SndngMailVO sndngMailVO = new SndngMailVO();
//        sndngMailVO.setSndngResultCode("C"); // 발송결과코드를 '요청'으로 설정
//        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
//        testData(sndngMailVO, loginVO);
//
//        // when
//        ComDefaultVO comDefaultVO = new ComDefaultVO();
//        int result = 0;
//        try {
//            result = sndngMailDtlsDAO.selectSndngMailListTotCnt(comDefaultVO);
//            if (log.isDebugEnabled()) {
//                log.debug("sndngMailVO, result = {}, {}", sndngMailVO, result);
//            }
//        } catch (Exception e) {
//            log.error("Exception selectSndngMailListCnt");
//            fail("Exception selectSndngMailListCnt");
//            // error(e);
//        }
//
//        // then
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 1, result);
//    }

}
