package egovframework.com.cop.cmy.service.impl;

import static org.junit.Assert.assertEquals;

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
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.cmy.service.Community;
import egovframework.com.cop.cmy.service.CommunityUser;
import egovframework.com.cop.cmy.service.CommunityUserVO;
import egovframework.com.cop.cmy.service.CommunityVO;
import egovframework.com.test.EgovTestAbstractDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 커뮤니티 사용자관리 DAO 단위 테스트
 *
 * @author 주레피
 *
 */

@ContextConfiguration(classes = { EgovTestAbstractDAO.class, EgovCommuManageDAOTest.class, })

@Configuration

@ImportResource({

        "classpath*:egovframework/spring/com/idgn/context-idgn-Cmmnty.xml",

})

@ComponentScan(

        useDefaultFilters = false,

        basePackages = {

                "egovframework.com.cop.cmy.service.impl",

        },

        includeFilters = {

                @Filter(

                        type = FilterType.ASSIGNABLE_TYPE,

                        classes = {

                                EgovCommuMasterDAO.class,

                                EgovCommuManageDAO.class,

                        }

                )

        }

)

@RequiredArgsConstructor
@Slf4j
// @Commit
public class EgovCommuManageDAOTest extends EgovTestAbstractDAO {

    /**
     * EgovCommuBBSMasterDAO
     */
    @Autowired
    private EgovCommuMasterDAO egovCommuMasterDAO;

    /**
     * EgovCommuBBSMasterDAO
     */
    @Autowired
    private EgovCommuManageDAO egovCommuManageDAO;

    /**
     * egovCmmntyIdGnrService
     */
    @Autowired
    @Qualifier("egovCmmntyIdGnrService")
    private EgovIdGnrService egovCmmntyIdGnrService;

    /**
     * 테스트 사용자 생성
     *
     */
    private void testUser(final CommunityUser cmmntyUser, final LoginVO loginVO) {
        final Community community = new Community();
        testData(community, loginVO);

        cmmntyUser.setCmmntyId(community.getCmmntyId());

        if (loginVO != null) {
            cmmntyUser.setEmplyrId(loginVO.getUniqId());
            cmmntyUser.setFrstRegisterId(loginVO.getUniqId());
            cmmntyUser.setLastUpdusrId(loginVO.getUniqId());
        }

        cmmntyUser.setMngrAt("Y");
        cmmntyUser.setUseAt("Y");

        egovCommuManageDAO.insertCommuUserRqst(cmmntyUser);
    }

    /**
     * 테스트 커뮤니티, 테스트 사용자 생성
     *
     */
    private void testData(final Community community, final LoginVO loginVO) {
        // 커뮤니티ID 설정
        try {
            community.setCmmntyId(egovCmmntyIdGnrService.getNextStringId());
        } catch (FdlException e) {
            log.error("FdlException egovCmmntyIdGnrService");
//            fail("FdlException egovCmmntyIdGnrService");
        }

//        // 커뮤니티명 설정
//        community.setCmmntyNm("테스트 커뮤니티");
//
//        // 커뮤니티소개 설정
//        community.setCmmntyIntrcn("테스트 커뮤니티입니다.");
//
//        // 사용여부 설정
//        community.setUseAt("Y");
//
//        // 등록구분코드 설정
//        community.setRegistSeCode("REGC02"); // 커뮤니티 등록
//
//        // 템플릿ID
//        community.setTmplatId("TMPT02"); // 커뮤니티 템플릿

        if (loginVO != null) {
            // 최초등록자ID 설정
            community.setFrstRegisterId(loginVO.getUniqId());
            community.setLastUpdusrId(loginVO.getUniqId());
        }

        // 커뮤니티 등록
        egovCommuMasterDAO.insertCommuMaster(community);

//        // 커뮤니티ID 설정
//        communityUser.setCmmntyId(community.getCmmntyId());
//
//        // 테스트 사용자 생성
//        testUser(communityUser);
    }

    /**
     * testSelectSingleCommuUserDetail
     */
    @Test
    public void testSelectSingleCommuUserDetail() {
        // given
        final CommunityUser cmmntyUser = new CommunityUser();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        testUser(cmmntyUser, loginVO);

        // when
        final CommunityUser resultCommunityUser = egovCommuManageDAO.selectSingleCommuUserDetail(cmmntyUser);

        if (log.isDebugEnabled()) {
            log.debug("cmmntyUser, resultCommunityUser = {}, {}", cmmntyUser, resultCommunityUser);
            log.debug("getMngrAt = {}, {}", cmmntyUser.getMngrAt(), resultCommunityUser.getMngrAt());
            log.debug("getUseAt = {}, {}", cmmntyUser.getUseAt(), resultCommunityUser.getUseAt());
        }

        // then
        assert1(cmmntyUser, resultCommunityUser);
    }

    private void assert1(final CommunityUser cmmntyUser, final CommunityUser resultCommunityUser) {
        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), cmmntyUser.getMngrAt(),
                resultCommunityUser.getMngrAt());
        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), cmmntyUser.getUseAt(),
                resultCommunityUser.getUseAt());
    }

    /**
     * testSelectCommuManagerList
     */
    @Test
    public void testSelectCommuManagerList() {
        // given
        final CommunityUser cmmntyUser = new CommunityUser();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        testUser(cmmntyUser, loginVO);

        final CommunityVO cmmntyVO = new CommunityVO();
        cmmntyVO.setCmmntyId(cmmntyUser.getCmmntyId());

        // when
        final List<CommunityUser> resultList = egovCommuManageDAO.selectCommuManagerList(cmmntyVO);
        // log.info("resultList=[{}]", resultList);
        for (final CommunityUser result : resultList) {
            if (log.isDebugEnabled()) {
                log.debug("result={}", result);
                log.debug("getEmplyrId={}, {}", cmmntyUser.getEmplyrId(), result.getEmplyrId());
                log.debug("getEmplyrNm={}, {}", cmmntyUser.getEmplyrNm(), result.getEmplyrNm());
            }

            // then
            assertSelectCommuManagerList(cmmntyUser, result);
        }
    }

    private void assertSelectCommuManagerList(final CommunityUser cmmntyUser, final CommunityUser result) {
        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), cmmntyUser.getEmplyrId(), result.getEmplyrId());
    }

//    @Test
//    public void testCheckExistUser() {
//        // given
//        CommunityUser communityUser = new CommunityUser();
//        testData(communityUser);
//
//        // when
//        int result = egovCommuManageDAO.checkExistUser(communityUser);
//
//        // then
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 1, result);
//
//        // given
//        CommunityUser communityNoUser = new CommunityUser();
//        communityNoUser.setCmmntyId(communityUser.getCmmntyId());
//        communityNoUser.setEmplyrId("00000000000"); // 존재하지 않는 사용자ID
//
//        // when
//        result = egovCommuManageDAO.checkExistUser(communityNoUser);
//
//        // then
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 0, result);
//    }

    /**
     * testInsertCommuUserRqst
     */
    @Test
    public void testInsertCommuUserRqst() {
        // given
        final Community community = new Community();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        testData(community, loginVO);

        final CommunityUser cmmntyUser = new CommunityUser();
        cmmntyUser.setCmmntyId(community.getCmmntyId());

        // when
        // testData()에서 사용자가 생성되었으므로 생성이 되지 않는다.
        final int result = egovCommuManageDAO.insertCommuUserRqst(cmmntyUser);

        // then
        // DuplicateKeyException 발생
        assertEquals(egovMessageSource.getMessage("fail.common.insert"), 1, result);
    }

//    @Test
//    public void testSelectCommuUserList() {
//        // given
//        CommunityUser communityUser = new CommunityUser();
//        testData(communityUser);
//
//        CommunityUserVO communityUserVO = new CommunityUserVO();
//        communityUserVO.setCmmntyId(communityUser.getCmmntyId());
//        communityUserVO.setFirstIndex(0); // LIMIT 0부터 가져 오기
//
//        // when
//        List<CommunityUser> resultList = egovCommuManageDAO.selectCommuUserList(communityUserVO);
//        log.info("resultList=[{}]", resultList);
//        for (final CommunityUser result : resultList) {
//            if (log.isDebugEnabled()) {
//                log.debug("result={}", result);
//            }
//        }
//
//        // then
//        assertTrue(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 0 < resultList.size());
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), communityUser.getEmplyrId(), resultList.get(0).getEmplyrId());
//
//        // given
//        CommunityUserVO noCommunityUserVO = new CommunityUserVO();
//        noCommunityUserVO.setCmmntyId("CMMNTY_00000000000000");
//        noCommunityUserVO.setFirstIndex(0); // LIMIT 0부터 가져 오기
//
//        // when
//        resultList = egovCommuManageDAO.selectCommuUserList(noCommunityUserVO);
//
//        // then
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 0, resultList.size());
//    }
//
//    @Test
//    public void testSelectCommuUserListCnt() {
//        // given
//        CommunityUser communityUser = new CommunityUser();
//        testData(communityUser);
//
//        CommunityUserVO communityUserVO = new CommunityUserVO();
//        communityUserVO.setCmmntyId(communityUser.getCmmntyId());
//        communityUserVO.setFirstIndex(0); // LIMIT 0부터 가져 오기
//
//        // when
//        int result = egovCommuManageDAO.selectCommuUserListCnt(communityUserVO);
//
//        // then
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 1, result);
//
//        // given
//        CommunityUserVO noCommunityUserVO = new CommunityUserVO();
//        noCommunityUserVO.setCmmntyId("CMMNTY_00000000000000");
//        noCommunityUserVO.setFirstIndex(0); // LIMIT 0부터 가져 오기
//
//        // when
//        result = egovCommuManageDAO.selectCommuUserListCnt(noCommunityUserVO);
//
//        // then
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 0, result);
//    }
//
//    @Test
//    public void testInsertCommuUser() {
//        // given
//        CommunityUser communityUser = new CommunityUser();
//        testData(communityUser);
//
//        CommunityUserVO communityUserVO = new CommunityUserVO();
//        communityUserVO.setCmmntyId(communityUser.getCmmntyId());
//        communityUserVO.setEmplyrId(communityUser.getEmplyrId());
//        communityUserVO.setMberSttus(communityUser.getMberSttus());
//        communityUserVO.setFirstIndex(0); // LIMIT 0부터 가져 오기
//
//        // when
//        egovCommuManageDAO.insertCommuUser(communityUserVO);
//
//        // then
//        List<CommunityUser> resultList = egovCommuManageDAO.selectCommuUserList(communityUserVO);
//        // log.debug("resultList = {}", resultList);
//
//        assertTrue(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 0 < resultList.size());
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), "A", communityUserVO.getMberSttus());
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), "P", resultList.get(0).getMberSttus());
//    }

    /**
     * testDeleteCommuUser
     */
    @Test
    public void testDeleteCommuUser() {
        // given
        final CommunityUser cmmntyUser = new CommunityUser();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        testUser(cmmntyUser, loginVO);

        final CommunityUserVO cmmntyUserVO = new CommunityUserVO();
        cmmntyUserVO.setCmmntyId(cmmntyUser.getCmmntyId());
        cmmntyUserVO.setEmplyrId(cmmntyUser.getEmplyrId());

        // when
        final int result = egovCommuManageDAO.deleteCommuUser(cmmntyUserVO);

        // then
        assertEquals(egovMessageSource.getMessage("fail.common.delete"), 1, result);
    }

    /**
     * testInsertCommuUserAdmin
     */
    @Test
    public void testInsertCommuUserAdmin() {
        // given
        final CommunityUser cmmntyUser = new CommunityUser();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        testUser(cmmntyUser, loginVO);

        final CommunityUserVO cmmntyUserVO = new CommunityUserVO();
        if (loginVO != null) {
            cmmntyUserVO.setLastUpdusrId(loginVO.getUniqId());
        }
        cmmntyUserVO.setCmmntyId(cmmntyUser.getCmmntyId());
        cmmntyUserVO.setEmplyrId(cmmntyUser.getEmplyrId());

        // when
        // TODO 이백행 2023-08-05 insertCommuUserAdmin 을 updateCommuUserAdmin 로 수정
        final int result = egovCommuManageDAO.insertCommuUserAdmin(cmmntyUserVO);

        // then
        assertEquals(egovMessageSource.getMessage("fail.common.update"), 1, result);
    }

    /**
     * testDeleteCommuUserAdmin
     */
    @Test
    public void testDeleteCommuUserAdmin() {
//        // given
//        CommunityUser communityUser = new CommunityUser();
//        communityUser.setMngrAt("Y");    // 관리자
//        communityUser.setMberSttus("P"); // 가입 승인 상태
//        testData(communityUser);

        final CommunityUser cmmntyUser = new CommunityUser();
        final LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        testUser(cmmntyUser, loginVO);
//
        final CommunityUserVO communityUserVO = new CommunityUserVO();
//        communityUserVO.setCmmntyId(communityUser.getCmmntyId());
//        communityUserVO.setEmplyrId(communityUser.getEmplyrId());
//        communityUserVO.setMngrAt(communityUser.getMngrAt());
//        communityUserVO.setFirstIndex(0); // LIMIT 0부터 가져 오기

        if (loginVO != null) {
            communityUserVO.setLastUpdusrId(loginVO.getUniqId());
        }

        communityUserVO.setCmmntyId(cmmntyUser.getCmmntyId());
        communityUserVO.setEmplyrId(cmmntyUser.getEmplyrId());

//        // when
        final int result = egovCommuManageDAO.deleteCommuUserAdmin(communityUserVO);
//
//        // then
//        List<CommunityUser> resultList = egovCommuManageDAO.selectCommuUserList(communityUserVO);
//        // log.debug("resultList = {}", resultList);
//
//        assertTrue(egovMessageSource.getMessage(FAIL_COMMON_SELECT), 0 < resultList.size());
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), "Y", communityUserVO.getMngrAt());
//        assertEquals(egovMessageSource.getMessage(FAIL_COMMON_SELECT), "N", resultList.get(0).getMngrAt());
        assertEquals(egovMessageSource.getMessage("fail.common.update"), 1, result);
    }
}
