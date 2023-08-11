DELETE FROM comtnclubuser WHERE clb_id = 'TEST_CLB_00000000001' AND cmmnty_id = 'TEST_CMMNTY_00000001' AND emplyr_id = 'USRCNFRM_00000000000';

DELETE FROM comtnclub WHERE clb_id = 'TEST_CLB_00000000001' AND cmmnty_id = 'TEST_CMMNTY_00000001';

INSERT INTO comtnclub (clb_id, cmmnty_id, clb_nm, use_at, frst_regist_pnttm, frst_register_id) VALUES ('TEST_CLB_00000000001', 'TEST_CMMNTY_00000001', 'test 이백행 2023-08-11 동호회명', 'Y', SYSDATE(), 'USRCNFRM_00000000000');

INSERT INTO comtnclubuser (clb_id, cmmnty_id, oprtr_at, use_at, frst_regist_pnttm, frst_register_id, emplyr_id) VALUES ('TEST_CLB_00000000001', 'TEST_CMMNTY_00000001', 'Y', 'Y', SYSDATE(), 'USRCNFRM_00000000000', 'USRCNFRM_00000000000');
