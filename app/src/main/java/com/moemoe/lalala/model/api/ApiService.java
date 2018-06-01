package com.moemoe.lalala.model.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moemoe.lalala.model.entity.*;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by yi on 2016/11/27.
 */

public interface ApiService {

    String URL_QINIU = "http://s.moemoe.la/";
    String REGISTER_PRICACE_URL = "http://s.moemoe.la/nonresponsibility.html";
    String LEVEL_DETAILS_URL = "http://s.moemoe.la/app/html/integral-v2.html";
    int LENGHT = 20;
    int LENGHT1 = 21;
    String SHARE_BASE = "http://2333.moemoe.la/share/doc/";
    String SHARE_BASE_DEBUG = "http:/183.131.152.216:8088/share/doc/";
    String SHARE_BAG = "http://2333.moemoe.la/share/folder";
    String SHARE_BAG_DEBUG = "http:/183.131.152.216:8088/share/folder";

    @GET("api/sys/getTime")
    Observable<ApiResult<Date>> getServerTime();

    @GET
    Observable<OldSimpleResult> getUrl(@Url String url);

    @PUT("api/user/sign")
    Observable<ApiResult<SignEntity>> signToday();

    @GET("api/app/check")
    Observable<ApiResult<AppUpdateEntity>> checkVersion(@Query("platform") String platform
            , @Query("version") int version);

    @POST("api/user/chagePwd")
    Observable<ApiResult> changePassword(@Body RegisterEntity entity);

    @POST("api/user/chageForgetPwd")
    Observable<ApiResult> resetPwdByCode(@Body RegisterEntity entity);

    @GET("api/tag/load/{tagId}")
    Observable<ApiResult<TagNodeEntity>> requestTagNode(@Path("tagId") String tagId);

    @GET("api/doc/tagDocs")
    Observable<ApiResult<ArrayList<DocListEntity>>> requestTagDocList(@Query("index") int index
            , @Query("size") int len
            , @Query("tag") String tagName
            , @Query("subTags") boolean subTags);

    @GET("api/doc/tagDocSwimPool")
    Observable<ApiResult<ArrayList<DocListEntity>>> requestSwimDocList(@Query("index") int index
            , @Query("size") int len
            , @Query("subTags") boolean subTags);

    @GET("api/doc/tagDocTops")
    Observable<ApiResult<ArrayList<DocListEntity>>> requestTopTagDocList(@Query("tag") String tagName);

    @GET("api/doc/tagDocHots")
    Observable<ApiResult<ArrayList<DocListEntity>>> requestHotTagDocList(@Query("tag") String tagName);

    @GET("api/cal/uiDocs")
    Observable<ApiResult<ArrayList<CalendarDayItemEntity>>> requestUiDocList(@Query("uiId") String u
            , @Query("index") int index
            , @Query("size") int len);

    @POST("v2/kira/upload/{suffix}")
    Observable<ApiResult<UploadEntity>> requestQnFileKey(@Path("suffix") String suffix);

    @POST("api/doc/addV2")
    Observable<ApiResult> createNormalDoc(@Body DocPut doc);

    @POST("api/doc/updateDoc/{docId}")
    Observable<ApiResult> updateDoc(@Body DocPut doc, @Path("docId") String docId);

    @POST("api/doc/addAutumnV2")
    Observable<ApiResult> createQiuMingShanDoc(@Body DocPut doc);

    @POST("api/doc/addSwimPoolV2")
    Observable<ApiResult> createSwimPoolDoc(@Body DocPut doc);

    @POST("api/doc/addArticle")
    Observable<ApiResult<String>> createWenZhangDoc(@Body DocPut doc);

    @GET("api/classroom/banner")
    Observable<ApiResult<ArrayList<BannerEntity>>> requestNewBanner(@Query("room") String room);

    @GET("api/classroom/featured")
    Observable<ApiResult<ArrayList<FeaturedEntity>>> requestFreatured(@Query("room") String room);

    @GET("api/cal/docs")
    Observable<ApiResult<DepartmentEntity>> requestDepartmentDocList(@Query("index") int index
            , @Query("size") int len
            , @Query("roomId") String roomId
            , @Query("before") String before);

    @POST("api/coinbox/give/{num}")
    Observable<ApiResult> donationCoin(@Path("num") long coin);

    @GET("api/coinbox/get")
    Observable<ApiResult<DonationInfoEntity>> getDonationInfo();

    @GET("api/coinbox/getRankList")
    Observable<ApiResult<DonationInfoEntity>> getBookDonationInfo(@Query("index") int index
            , @Query("size") int size);

    @POST("api/user/updateInfoV2")
    Observable<ApiResult> modifyAll(@Body ModifyEntity bean);

    @POST("api/code/sendRegisterV2")
    Observable<ApiResult> requestRegisterCode(@Body CodeEntity data);

    @POST("api/code/sendForgetV2")
    Observable<ApiResult> requestCode4ResetPwd(@Body CodeEntity mobile);

    @GET("api/user/{userId}/infoV3")
    Observable<ApiResult<UserInfo>> requestUserInfoV2(@Path("userId") String userId);

    @POST("api/report/save")
    Observable<ApiResult> report(@Body ReportEntity bean);

    @POST("api/user/loginMobile")
    Observable<ApiResult<LoginResultEntity>> login(@Body LoginEntity bean);

    @POST("api/user/loginOpenId")
    Observable<ApiResult<LoginResultEntity>> loginThird(@Body ThirdLoginEntity data);

    @GET("api/user/getReply")
    Observable<ApiResult<ArrayList<ReplyEntity>>> requestCommentFromOther(@Query("index") int index
            , @Query("size") int len);

    @GET("api/doc/{userId}/favoritesV2")
    Observable<ApiResult<ArrayList<PersonDocEntity>>> requestFavoriteDocList(@Path("userId") String userId, @Query("index") int index
            , @Query("size") int length);

    @GET("api/user/getDocsV2")
    Observable<ApiResult<ArrayList<PersonDocEntity>>> requestUserTagDocListV2(@Query("index") int index
            , @Query("size") int len
            , @Query("userId") String tagName);

    @POST("api/user/register")
    Observable<ApiResult> phoneRegister(@Body RegisterEntity bean);

    @POST("api/code/check")
    Observable<ApiResult> checkVCode(@Body RegisterEntity bean);

    @GET("api/doc/tagDocAutumns")
    Observable<ApiResult<ArrayList<DocListEntity>>> requestQiuMingShanDocList(@Query("index") int index
            , @Query("size") int length
            , @Query("subTags") boolean subTags);

    @PUT("api/user/logout")
    Observable<ApiResult> logout();

    @GET("api/tag/wallV2")
    Observable<ApiResult<ArrayList<WallBlock>>> getWallBlocksV2(@Query("page") int page);

    @GET("api/doc/loadV3/{docId}")
    Observable<ApiResult<DocDetailEntity>> requestNewDocContent(@Path("docId") String id);

    @DELETE("api/doc/del/{uuid}")
    Observable<ApiResult> deleteDoc(@Path("uuid") String uuid);

    @POST("api/doc/{docId}/favorite")
    Observable<ApiResult> favoriteDoc(@Path("docId") String id);

    @DELETE("api/doc/{docId}/favorite")
    Observable<ApiResult> cancelFavoriteDoc(@Path("docId") String id);

    @POST("api/doc/comment")
    Observable<ApiResult> sendNewComment(@Body CommentSendEntity bean);

    @POST("api/tag/dislike")
    Observable<ApiResult> dislikeNewTag(@Body TagLikeEntity bean);

    @POST("api/tag/like")
    Observable<ApiResult> likeNewTag(@Body TagLikeEntity bean);

    @POST("api/tag/add")
    Observable<ApiResult<String>> createNewTag(@Body TagSendEntity bean);

    @DELETE("api/doc/comment/{uuid}")
    Observable<ApiResult> deleteNewComment(@Path("uuid") String id);

    @PUT("api/doc/pay/{docId}")
    Observable<ApiResult> requestDocHidePath(@Path("docId") String id);

    @PUT("api/doc/giveCoin")
    Observable<ApiResult> giveCoinToDoc(@Body GiveCoinEntity bean);

    @GET("api/dustbinV2/getTextV2")
    Observable<ApiResult<ArrayList<TrashEntity>>> getTextTrashList(@Query("size") int size, @Query("timestamp") int timestamp);

    @GET("api/dustbinV2/getImageV2")
    Observable<ApiResult<ArrayList<TrashEntity>>> getImgTrashList(@Query("size") int size, @Query("timestamp") int timestamp);

    @PUT("api/dustbinV2/operation")
    Observable<ApiResult> operationTrash(@Body TrashOperationEntity entity);

    @POST("api/dustbinV2/{type}/favorite/{id}")
    Observable<ApiResult> favoriteTrash(@Path("type") String type, @Path("id") String id);

    @POST("api/dustbinV2/remove/{type}/favorite/{id}")
    Observable<ApiResult> cancelFavoriteTrash(@Path("type") String type, @Path("id") String id);

    @GET("api/dustbinV2/sotText")
    Observable<ApiResult<ArrayList<TrashEntity>>> myTextTrashList(@Query("index") int index, @Query("size") int size);

    @GET("api/dustbinV2/sotImage")
    Observable<ApiResult<ArrayList<TrashEntity>>> myImgTrashList(@Query("index") int index, @Query("size") int size);

    @GET("api/dustbinV2/favoriteText")
    Observable<ApiResult<ArrayList<TrashEntity>>> myFavoriteTextTrashList(@Query("index") int index, @Query("size") int size);

    @GET("api/dustbinV2/favoriteImage")
    Observable<ApiResult<ArrayList<TrashEntity>>> myFavoriteImageTrashList(@Query("index") int index, @Query("size") int size);

    @POST("api/dustbinV2/addText")
    Observable<ApiResult> createTextTrash(@Body TrashPut put);

    @POST("api/dustbinV2/addImage")
    Observable<ApiResult> createImageTrash(@Body TrashPut put);

    @GET("api/dustbinV2/text/top3")
    Observable<ApiResult<ArrayList<TrashEntity>>> getTextTop3();

    @GET("api/dustbinV2/image/top3")
    Observable<ApiResult<ArrayList<TrashEntity>>> getImageTop3();

    @POST("api/dustbinV2/tag/like")
    Observable<ApiResult> likeTrashTag(@Body TagLikeEntity entity);

    @POST("api/dustbinV2/tag/dislike")
    Observable<ApiResult> dislikeTrashTag(@Body TagLikeEntity entity);

    @POST("api/dustbinV2/addTag")
    Observable<ApiResult<String>> createTrashTag(@Body TagSendEntity entity);

    @POST("api/user/follow/{userId}")
    Observable<ApiResult> followUser(@Path("userId") String userId);

    @POST("api/user/unfollow/{userId}")
    Observable<ApiResult> cancelfollowUser(@Path("userId") String userId);

    @GET("api/user/{userId}/main/info")
    Observable<ApiResult<PersonalMainEntity>> getPersonalMain(@Path("userId") String id);

    @GET("api/user/followers")
    Observable<ApiResult<ArrayList<PersonFollowEntity>>> getUserFollowList(@Query("userId") String id, @Query("index") int index, @Query("size") int size);

    @GET("api/user/fans")
    Observable<ApiResult<ArrayList<PersonFollowEntity>>> getUserFansList(@Query("userId") String id, @Query("index") int index, @Query("size") int size);

    @POST("api/user/{show}/favorite")
    Observable<ApiResult> showFavorite(@Path("show") boolean show);

    @POST("api/user/{show}/follow")
    Observable<ApiResult> showFollow(@Path("show") boolean show);

    @POST("api/user/{show}/fans")
    Observable<ApiResult> showFans(@Path("show") boolean show);

    @GET("v2/kira/user/dailyTask")
    Observable<ApiResult<DailyTaskEntity>> getDailyTask();

    @POST("api/doc/shareDoc")
    Observable<ApiResult> shareDoc();

    @GET("api/user/coin/details")
    Observable<ApiResult<ArrayList<CoinDetailEntity>>> getCoinDetails(@Query("index") int index, @Query("size") int size);

    @GET("api/user/{userId}/comments")
    Observable<ApiResult<ArrayList<NewCommentEntity>>> getCommentsList(@Path("userId") String userId, @Query("index") int index, @Query("size") int len);

    @POST("api/user/comment")
    Observable<ApiResult> sendComment(@Body CommentListSendEntity entity);

    @POST("api/doc/lz/comment")
    Observable<ApiResult> delCommentByOwner(@Body DelCommentEntity entity);

    @GET("api/user/list/my/badge")
    Observable<ApiResult<ArrayList<BadgeEntity>>> requestMyBadge(@Query("index") int index, @Query("size") int size);

    @GET("api/user/list/all/badge")
    Observable<ApiResult<ArrayList<BadgeEntity>>> requestAllBadge(@Query("index") int index, @Query("size") int size);

    @POST("api/user/save/badge")
    Observable<ApiResult> saveBadge(@Body ArrayList<String> ids);

    @POST("api/user/buy/badge/{id}")
    Observable<ApiResult> buyBadge(@Path("id") String id);

    @GET("api/dustbinV2/image/tags/{id}")
    Observable<ApiResult<ArrayList<DocTagEntity>>> getImgTrashTags(@Path("id") String id);

    @GET("api/dustbinV2/text/tags/{id}")
    Observable<ApiResult<ArrayList<DocTagEntity>>> getTextTrashTags(@Path("id") String id);

    @GET("api/app/check/build")
    Observable<ApiResult<BuildEntity>> checkBuild(@Query("buildVersion") int buildVersion, @Query("appVersion") int appVersion);

    @POST("v2/kira/bag/open")
    Observable<ApiResult> openBag(@Body BagModifyEntity entity);


    @POST("v2/kira/bag/update")
    Observable<ApiResult> updateBag(@Body BagModifyEntity entity);

    @GET("api/bag/{userId}/info")
    Observable<ApiResult<BagEntity>> getBagInfo(@Path("userId") String userId);

    @POST("v2/kira/upload/check/md5")
    Observable<ApiResult<ArrayList<UploadResultEntity>>> checkMd5(@Body ArrayList<NewUploadEntity> uploadentities);

    @POST("api/bag/folder/add")
    Observable<ApiResult> createFolder(@Body BagFolderInfo entity);

    @POST("api/bag/folder/{folderId}/upload")
    Observable<ApiResult> uploadFolder(@Path("folderId") String folderId, @Body ArrayList<UploadResultEntity> files);

    @GET("v2/kira/bag/check/size")
    Observable<ApiResult<Boolean>> checkSize(@Query("size") long size);

    @GET("api/bag/folder/{folderId}/list")
    Observable<ApiResult<ArrayList<FileEntity>>> getFolderItemList(@Path("folderId") String id, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/bag/buy/list")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> getBagFavoriteList(@Query("size") int size, @Query("index") int index);

    @POST("v2/kira/bag/buy/delete")
    Observable<ApiResult> deleteBagFavoriteList(@Body ArrayList<String> folderIds);

    @POST("v2/kira/bag/report")
    Observable<ApiResult> reportBag(@Body ReportEntity bean);

    @POST("api/bag/folder/{folderId}/buy")
    Observable<ApiResult> buyFolder(@Path("folderId") String folderId);

    @POST("api/bag/folder/{folderId}/follow")
    Observable<ApiResult> followFolder(@Path("folderId") String folderId);

    @POST("api/bag/follow/delete")
    Observable<ApiResult> deleteBagFollowList(@Body ArrayList<String> ids);

    @POST("api/bag/folder/delete")
    Observable<ApiResult> deleteFolders(@Body ArrayList<String> folderIds);

    @POST("api/bag/folder/{folderId}/update")
    Observable<ApiResult> modifyFolder(@Path("folderId") String folderId, @Body BagFolderInfo.FolderInfo userBagFolder);

    @POST("api/bag/folder/{folderId}/file/delete")
    Observable<ApiResult> deleteFiles(@Path("folderId") String folderId, @Body ArrayList<String> fileIds);

    @POST("api/bag/folder/{folderId}/move")
    Observable<ApiResult> moveFiles(@Path("folderId") String folderId, @Body MoveFileEntity entity);

    @POST("v2/kira/bag/update/file/name/{type}")
    Observable<ApiResult> modifyFile(@Path("type") String type, @Body ModifyFileEntity entity);

    @POST("v2/kira/bag/file/copy")
    Observable<ApiResult> copyFile(@Body CopyFileEntity entity);

    @POST("api/tag/delete")
    Observable<ApiResult> delTags(@Body DelTagEntity entity);

    @POST("v2/kira/tag/remove")
    Observable<ApiResult> delTagsV2(@Body DelTagEntity entity);

    @POST("api/doc/reply/info")
    Observable<ApiResult<CommentDetailEntity>> getCommentDetail(@Body CommentDetailRqEntity entity);

    @GET("api/user/getSystemMsg")
    Observable<ApiResult<ArrayList<NetaMsgEntity>>> getSystemMsg(@Query("index") int index, @Query("size") int size);

    @GET("api/user/getNetaMsg")
    Observable<ApiResult<ArrayList<NetaMsgEntity>>> getNetaMsg(@Query("index") int index, @Query("size") int size);

    @GET("api/user/getAtMsg")
    Observable<ApiResult<ArrayList<NetaMsgEntity>>> getAtMsg(@Query("index") int index, @Query("size") int size);

    @POST("api/search/searchDoc")
    Observable<ApiResult<ArrayList<PersonDocEntity>>> getSearchDoc(@Body SearchEntity entity);

    @POST("v2/kira/search/searchBag")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> getSearchBag(@Body SearchEntity entity);

    @POST("api/search/searchUser")
    Observable<ApiResult<ArrayList<PersonFollowEntity>>> getSearchUser(@Body SearchEntity entity);

    @POST("api/search/searchKira")
    Observable<ApiResult<ArrayList<PersonFollowEntity>>> getSearchKira(@Body SearchEntity entity);

    @GET("api/talk/{open}/ignore/{talkId}")
    Observable<ApiResult> ignoreUser(@Path("talkId") String talkId, @Path("open") boolean open);

    @GET("api/user/list/activity")
    Observable<ApiResult<ArrayList<NetaEvent>>> getEventList();

    @POST("api/user/save/activity")
    Observable<ApiResult> saveEvent(@Body NetaEvent event);

    @GET("api/bag/{userId}/folder/info/{folderId}")
    Observable<ApiResult<BagDirEntity>> getFolder(@Path("userId") String userId, @Path("folderId") String folderId);

    @GET("api/user/getBrowseDoc")
    Observable<ApiResult<ArrayList<PersonDocEntity>>> getDocHistory(@Query("index") int index, @Query("size") int size);

    @POST("api/user/saveLive2d/{type}")
    Observable<ApiResult> saveLive2d(@Path("type") String type);

    @POST("api/user/save/black/{userId}")
    Observable<ApiResult> saveBlackUser(@Path("userId") String userId);

    @POST("api/user/remove/black/{userId}")
    Observable<ApiResult> removeBlackUser(@Path("userId") String userId);

    @GET("api/user/list/black")
    Observable<ApiResult<ArrayList<RejectEntity>>> getBlackList(@Query("index") int index, @Query("size") int size);

    @POST("api/doc/check/{docId}/egg")
    Observable<ApiResult<Boolean>> checkEgg(@Path("docId") String docId);

    @POST("api/doc/post/{docId}/egg")
    Observable<ApiResult> postEgg(@Path("docId") String docId);

    @POST("api/doc/remove/{docId}/egg")
    Observable<ApiResult> removeEgg(@Path("docId") String docId);

    @GET("api/shop/list")
    Observable<ApiResult<ArrayList<CoinShopEntity>>> loadShopList(@Query("index") int index, @Query("size") int size);

    @POST("api/shop/order/{id}")
    Observable<ApiResult<CreateOrderEntity>> createOrder(@Path("id") String id);

    @POST("api/shop/order/{num}/{id}")
    Observable<ApiResult<CreateOrderEntity>> createOrderNum(@Path("num") int num, @Path("id") String id);

    @POST("api/shop/orderV2/{num}/{id}")
    Observable<ApiResult<CreateOrderEntity>> createOrderNum(@Path("num") int num, @Path("id") String id, @Query("from") String from);

    @POST("api/user/save/address")
    Observable<ApiResult> saveUserAddress(@Body AddressEntity entity);

    @GET("api/user/find/address")
    Observable<ApiResult<AddressEntity>> loadUserAddress();

    @GET("api/shop/my/order")
    Observable<ApiResult<ArrayList<OrderEntity>>> loadOrderList(@Query("index") int index, @Query("size") int size);

    @POST("api/shop/cancel/{orderId}")
    Observable<ApiResult> cancelOrder(@Path("orderId") String orderId);

    @POST("api/shop/pay")
    Observable<ApiResult<PayResEntity>> payOrder(@Body PayReqEntity entity);

    @POST("api/shop/batch/order")
    Observable<ApiResult<ArrayList<JsonObject>>> createPayList(@Body OrderTmp orderTmp);

    @GET("api/app/check/txbb")
    Observable<ApiResult<Boolean>> checkTxbb();

    @POST("api/user/{follow}/follow/club/{clubId}")
    Observable<ApiResult> followClub(@Path("follow") boolean follow, @Path("clubId") String clubId);

    @POST("api/user/{follow}/follow/department/{departmentId}")
    Observable<ApiResult> followDepartment(@Path("follow") boolean follow, @Path("departmentId") String clubId);

    @GET("api/cal/{departmentId}/is/follow")
    Observable<ApiResult<Boolean>> isFollowDepartment(@Path("departmentId") String clubId);

    @GET("api/index/topUser")
    Observable<ApiResult<ArrayList<XianChongEntity>>> loadXianChongList();

    @GET("v2/kira/bag/{userId}/info")
    Observable<ApiResult<NewBagEntity>> loadBagData(@Path("userId") String userId);

    @GET("v2/kira/bag/{userId}/items")
    Observable<ApiResult<BagMyEntity>> loadBagMy(@Path("userId") String userId);

    @GET("v2/kira/bag/my/follow")
    Observable<ApiResult<BagMyEntity>> loadBagMyFollow();

    @POST("v2/kira/bag/update/{id}/folder")
    Observable<ApiResult> updateFolder(@Path("id") String folderId, @Body FolderRepEntity entity);

    @POST("v2/kira/bag/add/folder")
    Observable<ApiResult> createFolder(@Body FolderRepEntity entity);

    @GET("v2/kira/bag/{userId}/{type}/folder/list")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadFolderList(@Path("userId") String userId, @Path("type") String type, @Query("size") int size, @Query("index") int index);

    @POST("v2/kira/bag/delete/{type}/folder")
    Observable<ApiResult> deleteFolders(@Body ArrayList<String> folderIds, @Path("type") String type);

    @POST("v2/kira/bag/top/{id}/folder")
    Observable<ApiResult> topFolder(@Path("id") String folderId);

    @GET("v2/kira/bag/{userId}/{type}/folder/{folderId}/info")
    Observable<ApiResult<NewFolderEntity>> loadFolderInfo(@Path("userId") String userId, @Path("type") String type, @Path("folderId") String folderId);

    @GET("v2/kira/bag/{userId}/{type}/folder/{folderId}/file/list")
    Observable<ApiResult<JsonArray>> loadFileList(@Path("userId") String userId, @Path("type") String type, @Path("folderId") String folderId, @Query("size") int size, @Query("index") int index);

    @GET("v2/kira/bag/find/{userId}/{folderType}/favorite/files")
    Observable<ApiResult<ArrayList<StreamFileEntity>>> loadFavFileList(@Path("userId") String userId, @Path("folderType") String folderType, @Query("size") int size, @Query("index") int index);

    @GET("v2/kira/bag/{userId}/cartoon/{parentFolderId}/folder/list")
    Observable<ApiResult<ArrayList<ManHua2Entity>>> loadFiManHua2List(@Path("userId") String userId, @Path("parentFolderId") String parentFolderId, @Query("size") int size, @Query("index") int index);

    @POST("v2/kira/bag/delete/{type}/{folderId}/file")
    Observable<ApiResult> deleteFiles(@Body ArrayList<String> fileIds, @Path("type") String type, @Path("folderId") String folderId);

    @POST("v2/kira/bag/delete/{parentFolderId}/cartoon/folder")
    Observable<ApiResult> deleteManHua2(@Body ArrayList<String> fileIds, @Path("parentFolderId") String parentFolderId);

    @POST("v2/kira/bag/top/{parentFolderId}/cartoon/{folderId}/folder")
    Observable<ApiResult> topManHua2(@Path("parentFolderId") String parentFolderId, @Path("folderId") String folderId);

    @POST("v2/kira/bag/top/{folderId}/folder/{type}/file/{fileId}")
    Observable<ApiResult> topFile(@Path("folderId") String folderId, @Path("type") String type, @Path("fileId") String fileId);

    @POST("v2/kira/bag/remove/{userId}/folder/{type}/{folderId}/follow")
    Observable<ApiResult> removeFollowFolder(@Path("userId") String userId, @Path("type") String type, @Path("folderId") String folderId);

    @POST("v2/kira/bag/{userId}/folder/{type}/{folderId}/follow")
    Observable<ApiResult> followFolder(@Path("userId") String userId, @Path("type") String type, @Path("folderId") String folderId);

    @POST("v2/kira/bag/{userId}/folder/{type}/{folderId}/buy")
    Observable<ApiResult> buyFolder(@Path("userId") String userId, @Path("type") String type, @Path("folderId") String folderId);

    @POST("v2/kira/bag/folder/fiction/{folderId}/upload")
    Observable<ApiResult> uploadXiaoshuo(@Path("folderId") String folderId, @Body ArrayList<UploadResultEntity> entities);

    @POST("v2/kira/bag/folder/image/{folderId}/upload")
    Observable<ApiResult> uploadTuji(@Path("folderId") String folderId, @Body ArrayList<UploadResultEntity> entities);

    @POST("v2/kira/bag/folder/synthesize/{folderId}/upload")
    Observable<ApiResult> uploadZonghe(@Path("folderId") String folderId, @Body ArrayList<UploadResultEntity> entities);

    @POST("v2/kira/bag/save/{folderId}/viedoFile")
    Observable<ApiResult> uploadShipin(@Path("folderId") String folderId, @Body UploadResultEntity entities);

    @POST("v2/kira/bag/save/{folderId}/musicFile")
    Observable<ApiResult> uploadYinyue(@Path("folderId") String folderId, @Body ArrayList<UploadResultEntity> entities);

    @POST("v2/kira/bag/update/{parentFolderId}/cartoon/{id}/folder")
    Observable<ApiResult> uploadManhua2(@Path("parentFolderId") String parentFolderId, @Path("id") String folderId, @Body ManHuaUploadEntity entities);

    @POST("v2/kira/bag/add/{parentFolderId}/cartoon/folder")
    Observable<ApiResult> uploadManhua(@Path("parentFolderId") String parentFolderId, @Body ManHuaUploadEntity entities);

    @GET("v2/kira/bag/my/dynamic")
    Observable<ApiResult<ArrayList<DynamicTopEntity>>> loadDynamicTop();

    @GET("v2/kira/bag/{userId}/article/list")
    Observable<ApiResult<ArrayList<WenZhangFolderEntity>>> loadWenZhangList(@Path("userId") String userId, @Query("size") int size, @Query("index") int index);

    @GET("v2/kira/bag/{userId}/favorite/article/list")
    Observable<ApiResult<ArrayList<WenZhangFolderEntity>>> loadWenZhangFollowList(@Path("userId") String userId, @Query("size") int size, @Query("index") int index);

    @POST("v2/kira/bag/delete/cartoon/{parentFolderId}/{folderId}/file")
    Observable<ApiResult> deleteManHuaFile(@Path("parentFolderId") String parentId, @Path("folderId") String folderId, @Body ArrayList<String> fileIds);

    @GET("v2/kira/bag/follow/{type}/folder/list")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadFollowFolderList(@Path("type") String type, @Query("size") int size, @Query("index") int index);

    @GET("v2/kira/bag/my/follow/feed/{lastTime}")
    Observable<ApiResult<ArrayList<DynamicEntity>>> loadDynamicList(@Path("lastTime") long lastTime);

    @GET("v2/kira/bag/folder/recommend/reload/{excludeFolderId}")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadRefreshList(@Path("excludeFolderId") String excludeFolderId, @Query("folderName") String folderName, @Query("page") int page);

    @GET("v2/kira/user/getSampleUserInfo/{userId}")
    Observable<ApiResult<JsonObject>> loadSampleUserInfo(@Path("userId") String userId);

    @GET("v2/kira/user/eachOthers")
    Observable<ApiResult<ArrayList<PhoneMenuEntity>>> loadFollowListBoth(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/user/followers")
    Observable<ApiResult<ArrayList<PhoneMenuEntity>>> loadFollowListFollow(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/user/fans")
    Observable<ApiResult<ArrayList<PhoneMenuEntity>>> loadFollowListFans(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/user/getRcToken")
    Observable<ApiResult<String>> loadRcToken();

    @GET("v2/kira/mobile/get/role/likes")
    Observable<ApiResult<ArrayList<PhoneMateEntity>>> loadMateList();

    @GET("v2/kira/mobile/get/{role}/clothes")
    Observable<ApiResult<ArrayList<PhoneFukuEntity>>> loadFukuList(@Path("role") String role);

    @POST("v2/kira/mobile/set/{role}/clothes/{clothesId}")
    Observable<ApiResult> setFuku(@Path("role") String role, @Path("clothesId") String clothesId);

    @POST("v2/kira/mobile/set/{role}/to/deskmate")
    Observable<ApiResult> setMate(@Path("role") String role);

    @GET("v2/kira/tag/{targetId}/all")
    Observable<ApiResult<ArrayList<DocTagEntity>>> loadTags(@Path("targetId") String id);

    @POST("v2/kira/tag/add")
    Observable<ApiResult<String>> sendTag(@Body TagSendEntity entity);

    @POST("v2/kira/tag/{targetId}/{like}/like/{tagId}")
    Observable<ApiResult> plusTag(@Path("like") boolean like, @Path("targetId") String targetId, @Path("tagId") String tagId);

    @GET("v2/kira/dynamic/rt")
    Observable<ApiResult<ArrayList<CommentV2Entity>>> loadRtComment(@Query("dynamicId") String dynamicId, @Query("size") int size, @Query("index") int index);

    @GET("v2/kira/comment/get/{orderBy}/{targetId}/comments")
    Observable<ApiResult<ArrayList<CommentV2Entity>>> loadComment(@Path("targetId") String targetId, @Path("orderBy") String orderBy, @Query("size") int size, @Query("start") int index);

    @GET("v2/kira/comment/get/sec/{orderBy}/{targetId}/comments")
    Observable<ApiResult<ArrayList<CommentV2SecEntity>>> loadCommentSec(@Path("targetId") String targetId, @Path("orderBy") String orderBy, @Query("size") int size, @Query("start") int index);

    @POST("v2/kira/comment/remove/{type}/{targetId}/comment/{commentId}")
    Observable<ApiResult> deleteComment(@Path("targetId") String targetId, @Path("type") String type, @Path("commentId") String commentId);

    @POST("v2/kira/comment/{parentId}/sec/{targetId}/remove/{commentId}/comment")
    Observable<ApiResult> deleteCommentSec(@Path("targetId") String targetId, @Path("parentId") String parentId, @Path("commentId") String commentId);

    @POST("v2/kira/comment/like/{flag}/{targetId}/comment/{commentId}")
    Observable<ApiResult> favoriteComment(@Path("targetId") String targetId, @Path("flag") boolean flag, @Path("commentId") String commentId);

    @POST("v2/kira/comment/like/sec/{flag}/{targetId}/comment/{commentId}")
    Observable<ApiResult> favoriteCommentSec(@Path("targetId") String targetId, @Path("flag") boolean flag, @Path("commentId") String commentId);

    @POST("v2/kira/comment/send/{targetId}/comment")
    Observable<ApiResult> sendComment(@Path("targetId") String targetId, @Body CommentSendV2Entity entity);

    @POST("v2/kira/comment/send/movie/{movieId}/comment")
    Observable<ApiResult> sendVideoComment(@Path("movieId") String targetId, @Body CommentSendV2Entity entity);

    @POST("v2/kira/comment/send/doc/{docId}/comment")
    Observable<ApiResult> sendCommentWenZhang(@Path("docId") String targetId, @Body CommentSendV2Entity entity);

    @POST("v2/kira/comment/send/sec/{commentId}/comment")
    Observable<ApiResult> sendCommentSec(@Path("commentId") String commentId, @Body CommentSendV2Entity entity);

    @POST("v2/kira/dynamic/shareArticle")
    Observable<ApiResult> shareArticle(@Body ShareArticleSendEntity entity);

    @POST("v2/kira/dynamic/shareFolder")
    Observable<ApiResult> shareFolder(@Body ShareFolderSendEntity entity);

    @POST("v2/kira/dynamic/shareFile/{fileType}")
    Observable<ApiResult> shareStream(@Path("fileType") String fileType, @Body ShareStreamSendEntity entity);

    @POST("v2/kira/dynamic/retweet")
    Observable<ApiResult<Float>> rtDynamic(@Body ForwardSendEntity entity);

    @POST("v2/kira/dynamic/send")
    Observable<ApiResult> createDynamic(@Body DynamicSendEntity entity);

    @GET("v2/kira/comment/get/{targetId}/top")
    Observable<ApiResult<ArrayList<CommentV2Entity>>> loadTopComment(@Path("targetId") String id);

    @POST("v2/kira/dynamic/deleteDynamic/{dynamicId}/{type}")
    Observable<ApiResult> deleteDynamic(@Path("dynamicId") String dynamicId, @Path("type") String type);

    @POST("v2/kira/dynamic/reward/{dynamicId}/num/{num}")
    Observable<ApiResult> giveCoinToDynamic(@Path("dynamicId") String id, @Path("num") int num);

    @GET("v2/kira/dynamic/getDynamicList/follow")
    Observable<ApiResult<ArrayList<NewDynamicEntity>>> loadFeedFollowList(@Query("time") long timestamp);

    @GET("v2/kira/dynamic/getMyFavDynamicList")
    Observable<ApiResult<ArrayList<NewDynamicEntity>>> loadFeedFavoriteList(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/dynamic/getDynamicList/random")
    Observable<ApiResult<ArrayList<NewDynamicEntity>>> loadFeedRandomList(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/dynamic/getDynamicList/playground")
    Observable<ApiResult<ArrayList<NewDynamicEntity>>> loadFeedGroundList(@Query("time") long timestamp);

    @GET("v2/kira/dynamic/get/{userId}/dynamic/list")
    Observable<ApiResult<ArrayList<NewDynamicEntity>>> loadFeedMyList(@Path("userId") String userId, @Query("time") long timestamp);

    @GET("v2/kira/user/get/tickets")
    Observable<ApiResult<Integer>> loadTicketsNum();

    @POST("v2/kira/sound/unlock/{soundId}")
    Observable<ApiResult> unlockLuYin(@Path("soundId") String id);

    @GET("v2/kira/sound/sounds")
    Observable<ApiResult<ArrayList<LuYinEntity>>> loadLuYinList(@Query("type") String type, @Query("roleName") String roleName, @Query("index") int index, @Query("size") int size);

    @POST("v2/kira/dynamic/saveMyFavDynamic/{dynamicId}")
    Observable<ApiResult> collectDynamic(@Path("dynamicId") String dynamicId);

    @POST("v2/kira/dynamic/deleteFavDynamic/{id}")
    Observable<ApiResult> cancelCollectDynamic(@Path("id") String id);

    @POST("v2/kira/dynamic/saveDynamicReport")
    Observable<ApiResult> reportDynamic(@Body ReportEntity entity);

    @GET("v2/kira/user/getUserInfoByUserNo")
    Observable<ApiResult<ArrayList<InviteUserEntity>>> loadInviteList();

    @GET("v2/kira/user/getUserNameByUserNo/{userno}")
    Observable<ApiResult<String>> getUserNameByNo(@Path("userno") String no);

    @POST("v2/kira/user/inviteUserByUserNO/{userno}")
    Observable<ApiResult> useInviteNo(@Path("userno") String userno);

    @GET("v2/kira/story/find/all")
    Observable<ApiResult<ArrayList<JuQIngStoryEntity>>> getAllStory();

    @GET("v2/kira/story/find/story/version")
    Observable<ApiResult<Integer>> checkStoryVersion();

    @GET("v2/kira/story/find/trigger/all")
    Observable<ApiResult<ArrayList<JuQingTriggerEntity>>> getAllTrigger();

    @POST("v2/kira/story/save/record/{storyId}")
    Observable<ApiResult<Long>> doneStory(@Path("storyId") String id);

    @GET("v2/kira/story/find/{level}/group/{type}")
    Observable<ApiResult<ArrayList<JuQingEntity>>> loadStoryList(@Path("level") int level, @Path("type") int type, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/story/find/my/story")
    Observable<ApiResult<ArrayList<JuQingDoneEntity>>> getDoneJuQing();

    @GET("v2/kira/doc/get/department")
    Observable<ApiResult<ArrayList<SubmissionDepartmentEntity>>> loadDepartment();

    @POST("v2/kira/doc/submit/contribute")
    Observable<ApiResult> submission(@Body SendSubmissionEntity entity);

    @GET("v2/kira/doc/get/contribute")
    Observable<ApiResult<ArrayList<SubmissionItemEntity>>> loadSubmissionList(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/map/pics")
    Observable<ApiResult<ArrayList<MapEntity>>> loadMapPics();

    @GET("v2/kira/comment/get/hot")
    Observable<ApiResult<ArrayList<Comment24Entity>>> load24Comments(@Query("page") int page);

    @GET("v2/kira/bag/hot/folder")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> load24Folder();

    @GET("v2/kira/bag/hot/folderV2/{idx}")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadHotFolderV2(@Path("idx") int index);

    @GET("v2/kira/dynamic/get/{dynamicId}/dynamic")
    Observable<ApiResult<NewDynamicEntity>> getDynamic(@Path("dynamicId") String id);

    @GET("v2/kira/doc/get/doc/{departmentType}/list")
    Observable<ApiResult<ArrayList<DocResponse>>> loadOldDocList(@Path("departmentType") String type, @Query("timestamp") long timestamp);

    @GET("v2/kira/group/notify/list")
    Observable<ApiResult<ArrayList<GroupNoticeEntity>>> loadMsgList(@Query("index") int index, @Query("size") int size);

    @POST("v2/kira/group/push/{result}/for/{notifyId}")
    Observable<ApiResult> responseNotice(@Path("result") boolean result, @Path("notifyId") String id);

    @GET("v2/kira/group/list")
    Observable<ApiResult<ArrayList<GroupEntity>>> loadGroupList(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/group/my/list")
    Observable<ApiResult<ArrayList<GroupEntity>>> loadMyGroupList(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/group/{groupId}/info")
    Observable<ApiResult<GroupEntity>> loadGroup(@Path("groupId") String groupId);

    @POST("v2/kira/group/apply/join/{groupId}")
    Observable<ApiResult> applyJoinGroup(@Path("groupId") String groupId);

    @POST("v2/kira/group/quit/{groupId}")
    Observable<ApiResult> quitGroup(@Path("groupId") String groupId);

    @POST("v2/kira/group/dismiss/{groupId}")
    Observable<ApiResult> dismissGroup(@Path("groupId") String groupId);

    @POST("v2/kira/group/join/{groupId}")
    Observable<ApiResult> JoinAuthorGroup(@Path("groupId") String groupId);

    @POST("v2/kira/group/apply")
    Observable<ApiResult> createGroup(@Body GroupEditEntity editEntity);

    @POST("v2/kira/group/update/{groupId}/info")
    Observable<ApiResult> updateGroup(@Path("groupId") String groupId, @Body GroupEditEntity editEntity);

    @POST("v2/kira/group/invite/{userId}/join/{groupId}")
    Observable<ApiResult> inviteUserJoinGroup(@Path("userId") String userId, @Path("groupId") String groupId);

    @GET("v2/kira/group/{groupId}/user/list")
    Observable<ApiResult<ArrayList<UserTopEntity>>> loadGroupMemberList(@Path("groupId") String groupId, @Query("index") int index, @Query("size") int size);

    @POST("v2/kira/group/remove/member")
    Observable<ApiResult> delGroupMember(@Body GroupMemberDelEntity entity);

    @GET("v2/kira/map/list/place")
    Observable<ApiResult<ArrayList<MapAddressEntity>>> loadMapAddressList();

    @POST("v2/kira/user/saveUserGps")
    Observable<ApiResult> saveUserLocation(@Body UserLocationEntity entity);

    @POST("v2/kira/user/saveUserPic")
    Observable<ApiResult> saveUserMapImage(@Body UserMapSendEntity entity);

    @POST("v2/kira/user/saveUserPicCheck")
    Observable<ApiResult> checkUserMapImage(@Body UserMapSendEntity entity);

    @GET("v2/kira/map/list/all/user")
    Observable<ApiResult<ArrayList<MapEntity>>> loadMapAllUser();

    @GET("v2/kira/map/list/birthday/user")
    Observable<ApiResult<ArrayList<MapEntity>>> loadMapBirthdayUser();

    @GET("v2/kira/map/list/eachFollow/user")
    Observable<ApiResult<ArrayList<MapEntity>>> loadMapEachFollowUser();

    @GET("v2/kira/map/list/top/user")
    Observable<ApiResult<NearUserEntity>> loadMapTopUser();

    @GET("v2/kira/map/list/near/userV2")
    Observable<ApiResult<NearUserEntity>> loadMapNearUser(@Query("lat") double lat, @Query("lon") double lon);

    @GET("v2/kira/mobile/get/audio")
    Observable<ApiResult<ArrayList<Live2dMusicEntity>>> loadLive2dMusicList();

    @GET("v2/kira/map/list/all/sysUserPic")
    Observable<ApiResult<ArrayList<MapUserImageEntity>>> loadMapSelectList();

    @GET("v2/kira/user/list/{userId}/artwork")
    Observable<ApiResult<ArrayList<MapHistoryEntity>>> loadMapHistoryList(@Path("userId") String userId, @Query("index") int index, @Query("size") int size);

    @POST("v2/kira/dynamic/{flag}/{dynamicId}/like")
    Observable<ApiResult> likeDynamic(@Path("dynamicId") String dynamicId, @Path("flag") boolean flag);

    @POST("v2/kira/live2d/buy/{id}")
    Observable<ApiResult> buyLive2d(@Path("id") String id);

    @GET("v2/kira/live2d/list")
    Observable<ApiResult<ArrayList<Live2dShopEntity>>> loadLive2dList(@Query("index") int index, @Query("size") int size);

    @POST("v2/kira/live2d/score/{num}/for/{id}")
    Observable<ApiResult> pingfenLive2d(@Path("num") int num, @Path("id") String id);

    @GET("v2/kira/dynamic/getDynamicList/randomV2")
    Observable<ApiResult<ArrayList<DiscoverEntity>>> loadDiscoverList(@Query("minIdx") long minIdx, @Query("maxIdx") long maxIdx);

    @GET("v2/kira/dynamic/getDynamicList/followV2")
    Observable<ApiResult<ArrayList<DiscoverEntity>>> loadFollowList(@Query("time") long time);

    @POST("v2/kira/user/pic/{flag}/likeV2/for/{artworkId}")
    Observable<ApiResult> likeUserMapRole(@Path("flag") boolean flag, @Path("artworkId") String artworkId);

    @POST("v2/kira/user/remove/artwork")
    Observable<ApiResult> deleteHistoryMapRole(@Body SimpleListSend object);

    @GET("v2/kira/app/get/all")
    Observable<ApiResult<ArrayList<SplashEntity>>> loadSplashList();

    @GET("v2/kira/fx/roles")
    Observable<ApiResult<ArrayList<ShareLive2dEntity>>> loadShareLive2dList();

    @POST("v2/kira/kpi/department/{id}/click")
    Observable<ApiResult> clickDepartment(@Path("id") String id);

    @POST("v2/kira/kpi/department/{id}/{time}/stay")
    Observable<ApiResult> stayDepartment(@Path("id") String id, @Path("time") int time);

    @POST("v2/kira/kpi/fx/{type}")
    Observable<ApiResult> shareKpi(@Path("type") String type);

    @GET("v2/kira/dynamic/notify/list/{timestamp}")
    Observable<ApiResult<ArrayList<FeedNoticeEntity>>> loadFeedNoticeList(@Path("timestamp") long timestamp);

    @GET("v2/kira/dynamic/getDynamicList/{type}/followV3")
    Observable<ApiResult<ArrayList<FeedNoticeEntity>>> loadFeedNoticeListV3(@Path("type") String type, @Query("followTime") long followTime, @Query("notifyTime") long notifyTime);

    @GET("v2/kira/dynamic/getDynamicList/followV4")
    Observable<ApiResult<ArrayList<DiscoverEntity>>> loadFeedNoticeListV4(@Query("time") long time);

    @GET("v2/kira/doc/get/department/part")
    Observable<ApiResult<ArrayList<LuntanTabEntity>>> loadLuntanTabList();

    @POST("api/doc/addV3")
    Observable<ApiResult> createDocV3(@Body DocPut doc);

    @GET("v2/kira/doc/get/department/{departmentId}/list")
    Observable<ApiResult<ArrayList<DocResponse>>> loadDepartmentDocList(@Path("departmentId") String departmentId, @Query("timestamp") long timestamp);

    @GET("v2/kira/group/department/{departmentId}")
    Observable<ApiResult<ArrayList<DepartmentGroupEntity>>> loadDepartmentGroup(@Path("departmentId") String departmentId);

    @GET("v2/kira/mobile/get/ar/stick")
    Observable<ApiResult<ArrayList<StickEntity>>> loadStickList();

    @POST("v2/kira/mobile/buy/ar/stick")
    Observable<ApiResult> buyStick(@Body StickSend object);

    @GET("v2/kira/game/{userId}/resurgence/num")
    Observable<ApiResult<Integer>> getFuHuoNum(@Path("userId") String id);

    @POST("v2/kira/game/{userId}/exchange/{num}")
    Observable<ApiResult> useCiYuanBiGetFuHuo(@Path("userId") String id, @Path("num") int num);

//    @GET("v2/kira/bag/newest/folder")
//    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadNewFolder(@Query("index")int index,@Query("size")int size);

    //ALL:全部 XS:小说 MH:漫画 TJ:图集 ZH:综合 MOVIE:视频 MUSIC:音乐)
    @GET("v2/kira/bag/newest/v2/{type}/bag")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadNewFolderV2(@Path("type") String type, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/dynamic/getDynamicList/randomV3")
    Observable<ApiResult<ArrayList<DiscoverEntity>>> loadHotDynamicList(@Query("minIdx") long minIdx, @Query("maxIdx") long maxIdx);

    @GET("v2/kira/user/list/recommend")
    Observable<ApiResult<ArrayList<FeedRecommendUserEntity>>> loadFeedRecommentUserList();

    @GET("v2/kira/tag/match/all/type/{type}")
    Observable<ApiResult<ArrayList<RecommendTagEntity>>> loadRecommendTag(@Path("type") String type);

    @GET("v2/kira/tag/match/all/{word}")
    Observable<ApiResult<ArrayList<RecommendTagEntity>>> loadKeywordTag(@Path("word") String word);

    @GET("api/shop/product/{id}")
    Observable<ApiResult<CoinShopEntity>> loadShopDetail(@Path("id") String id);

    @GET("v2/kira/game/{userId}/has/{gameId}/{roleId}/status")
    Observable<ApiResult<Boolean>> hasRole(@Path("userId") String userId, @Path("gameId") String gameId, @Path("roleId") String roleId);

    @POST("v2/kira/user/saveUserText")
    Observable<ApiResult> saveUserText(@Body SimpleRequestEntity entity);

    @GET("v2/kira/game/price/info")
    Observable<ApiResult<GamePriceInfoEntity>> getGamePriceInfo();

    @GET("v2/kira/dynamic/red/{dynamicId}")
    Observable<ApiResult<ArrayList<HongBaoEntity>>> loadHongBaoList(@Path("dynamicId") String id);

    @GET("v2/kira/tag/all/my/focus/list")
    Observable<ApiResult<ArrayList<UserFollowTagEntity>>> loadUserTags();

    @GET("v2/kira/tag/all/focus/list")
    Observable<ApiResult<ArrayList<OfficialTag>>> loadOfficialTags();

    @POST("v2/kira/tag/save/mine/focus/tags")
    Observable<ApiResult> saveUserTags(@Body SimpleListSend send);

    @POST("v2/kira/tag/apply/manager")
    Observable<ApiResult> applyTagAdmin(@Body CommonRequest send);

    @GET("v2/kira/tag/third/{tagId}/content")
    Observable<ApiResult<FeedFollowOther1Entity>> loadTagContent(@Path("tagId") String id);

    @GET("v2/kira/tag/third/{tagId}/content/video/list")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadTagMoiveList(@Path("tagId") String id, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/tag/third/{tagId}/content/folder/list")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadTagFolderList(@Path("tagId") String id, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/tag/third/{tagId}/content/article/list")
    Observable<ApiResult<ArrayList<FeedFollowType1Entity>>> loadTagArticleList(@Path("tagId") String id, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/tag/third/{tagId}/content/music/list")
    Observable<ApiResult<ArrayList<FeedFollowType1Entity>>> loadTagMusicList(@Path("tagId") String id, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/tag/third/{tagId}/content/doc/list")
    Observable<ApiResult<ArrayList<DocResponse>>> loadTagDocList(@Path("tagId") String id, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/tag/third/{tagId}/manager")
    Observable<ApiResult<FeedFollowType2Entity>> loadTagManager(@Path("tagId") String id);

    @GET("v2/kira/bag/video/wait/check")
    Observable<ApiResult<ArrayList<StreamFileEntity>>> loadVideoExamineList(@Query("index") int index, @Query("size") int size);

    @GET("v2/kira/tag/all/list")
    Observable<ApiResult<ArrayList<FeedFollowType1Entity>>> loadFollowAllList(@Query("index") int index, @Query("size") int size);

    @POST("v2/kira/tag/manager/delete/folder")
    Observable<ApiResult> delTagFolder(@Body TagFileDelRequest request);

    @POST("v2/kira/tag/manager/delete/{type}/file")
    Observable<ApiResult> delTagFile(@Path("type") String type, @Body TagFileDelRequest request);

    @GET("v2/kira/bag/search/{type}/bag")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadFeedBagSearchList(@Path("type") String type, @Query("name") String name, @Query("page") int page);

    @GET("v2/kira/bag/{folderId}/{fileId}/video/info")
    Observable<ApiResult<KiraVideoEntity>> loadVideoInfo(@Path("folderId") String folderId, @Path("fileId") String fileId);

    @GET("v2/kira/bag/{folderId}/{fileId}/music/info")
    Observable<ApiResult<KiraVideoEntity>> loadMusicInfo(@Path("folderId") String folderId, @Path("fileId") String fileId);

    @GET("v2/kira/bag/find/video/{videoId}/subtitle")
    Observable<ApiResult<String>> loadDanmaku(@Path("videoId") String id);

    @POST("v2/kira/bag/save/video/subtitle")
    Observable<ApiResult> sendDanmaku(@Body DanmakuSend videoReq);

    @POST("v2/kira/bag/favorite/{folderType}/{folderId}/file/{fileId}")
    Observable<ApiResult> favMoiveOrMusic(@Path("folderType") String folderType, @Path("folderId") String folderId, @Path("fileId") String fileId);

    @POST("v2/kira/bag/remove/{folderType}/favorite/{folderId}/file/{fileId}")
    Observable<ApiResult> cancelFavMoiveOrMusic(@Path("folderType") String folderType, @Path("folderId") String folderId, @Path("fileId") String fileId);

    @POST("v2/kira/bag/folder/{folderId}/file/{fileType}/{fileId}/buy")
    Observable<ApiResult> buyFile(@Path("folderId") String folderId, @Path("fileType") String fileType, @Path("fileId") String fileId);

    @GET("v2/kira/bag/visitor/list")
    Observable<ApiResult<ArrayList<UserTopEntity>>> loadVisitorInfo(@Query("userId") String userId, @Query("index") int index, @Query("size") int size);

    @POST("v2/kira/tag/join/{join}/{tagId}")
    Observable<ApiResult> loadTagJoin(@Path("join") boolean join, @Path("tagId") String tagId);

    @GET("v2/kira/tag/{tagId}/newest/bag")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> loadTagNewst(@Path("tagId") String tagId, @Query("size") int size, @Query("index") int index);

    @GET("v2/kira/doc/get/tag/{tagId}/list")
    Observable<ApiResult<ArrayList<DocResponse>>> loadDocTagList(@Path("tagId") String tagId, @Query("timestamp") long timestamp);

    @GET("v2/kira/doc/get/tag/top/{tagId}/list")
    Observable<ApiResult<ArrayList<DocResponse>>> loadDocTagTopList(@Path("tagId") String tagId);

    @POST("v2/kira/doc/like/{like}/{docId}")
    Observable<ApiResult> loadDocLike(@Path("like") boolean like, @Path("docId") String docId);

    @GET("v2/kira/tag/{tagId}/members")
    Observable<ApiResult<ArrayList<UserTopEntity>>> loadTagMembers(@Path("tagId") String tagId, @Query("size") int size, @Query("index") int index, @Query("condition") String condition);

    @GET("v2/kira/tag/{tagId}/managers/members")
    Observable<ApiResult<AllPersonnelEntity>> loadTagAllPersonnel(@Path("tagId") String tagId);

    @POST("v2/kira/tag/save/describe/{thiId}")
    Observable<ApiResult> loadSavaDescribe(@Path("thiId") String thiId, @Query("describe") String describe);

    @GET("v2/kira/tag/all/focus/list/V2")
    Observable<ApiResult<ArrayList<OfficialTag>>> loadTagAllList();

    @GET("v2/kira/doc/get/follow/list")
    Observable<ApiResult<ArrayList<DocResponse>>> loadFollowList(@Query("index") int index, @Query("size") int size);

    @POST("v2/kira/search/searchAll")
    Observable<ApiResult<SeachAllEntity>> loadSearchAllList(@Body SearchEntity entity);

    @POST("v2/kira/search/searchMate")
    Observable<ApiResult<ArrayList<UserTopEntity>>> loadSearchMateList(@Body SearchEntity entity);

    @POST("v2/kira/search/searchDynamic")
    Observable<ApiResult<ArrayList<NewDynamicEntity>>> loadSearchDynamicList(@Body SearchEntity entity);

    @POST("v2/kira/search/searchDocV2")
    Observable<ApiResult<ArrayList<DocResponse>>> loadSearchDocList(@Body SearchEntity entity);

    @POST("v2/kira/search/searchBagV2")
    Observable<ApiResult<ArrayList<ShowFolderEntity>>> LoadSearchBag(@Body SearchEntity entity);

    @GET("v2/kira/tag/match/all/type/{type}")
    Observable<ApiResult<ArrayList<RecommendTagEntity>>> loadRecommendTagV2(@Path("type") String type);

    @POST("v2/kira/doc/put/tag/{status}/top/{docId}")
    Observable<ApiResult> loadDocTop(@Path("status") boolean status, @Path("docId") String docId);

    @POST("v2/kira/doc/put/tag/{status}/excellent/{docId}")
    Observable<ApiResult> loadDocxcellent(@Path("status") boolean status, @Path("docId") String docId);

    @GET("v2/kira/comment/get/{targetId}/lz/comments")
    Observable<ApiResult<ArrayList<CommentV2Entity>>> loadGetCommentsLz(@Path("targetId") String targetId, @Query("size") int size, @Query("start") int start);

    @GET("v2/kira/doc/like/{targetId}/users")
    Observable<ApiResult<ArrayList<UserTopEntity>>> loadDocLikeUsers(@Path("targetId") String targetId, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/dynamic/like/{targetId}/users")
    Observable<ApiResult<ArrayList<UserTopEntity>>> loadDynamicLikeUsers(@Path("targetId") String targetId, @Query("index") int index, @Query("size") int size);

    @GET("v2/kira/user/list/recommend")
    Observable<ApiResult<ArrayList<PhoneMenuEntity>>> loadFeedRecommentUserListV2();

    @POST("api/doc/del/v2/{uuid}")
    Observable<ApiResult> deleteDocV2(@Path("uuid") String id, @Body ReportEntity bean);

    @GET("v2/kira/user/notify/{type}/list")
    Observable<ApiResult<ArrayList<FeedNoticeEntity>>> loadNotifyList(@Path("type") String type, @Query("notifyTime") long notifyTime);

    @POST("v2/kira/game/getPower/{userId}")
    Observable<ApiResult> loadGameShare(@Path("userId") String userId);

    @GET("v2/kira/user/friends/followers")
    Observable<ApiResult<ArrayList<PhoneMenuEntity>>> loadUserFriends(@Query("index") int index, @Query("size") int size);

    @POST("v2/kira/group/shield/group/{falg}")
    Observable<ApiResult> loadGroupShield(@Path("falg") boolean falg);

    @GET("v2/kira/story/find/my/story/{groupId}")
    Observable<ApiResult<ArrayList<StoryListEntity>>> loadStoryFindList(@Path("groupId") String groupId);


    @GET("v2/kira/house/list/all/user")
    Observable<ApiResult<ArrayList<HomeEntity>>> loadHomeAllUser();

    @GET("v2/kira/house/userDeskmate")
    Observable<ApiResult<UserDeskmateEntity>> loadHousUserDeskmate();

    @GET("v2/kira/house/inHouseFurnitures")
    Observable<ApiResult<ArrayList<MapEntity>>> loadHouseInHouseFurnitures();

    @GET("v2/kira/house/inHouseRubblish")
    Observable<ApiResult<ArrayList<MapEntity>>> loadHouseInHouseRubblish();
    
    @GET("v2/kira/house/inHouseRoles")
    Observable<ApiResult<ArrayList<MapEntity>>> loadHouseInHouseRoles();

    @GET("/v2/kira/house/allRolesForUserSelect")
    Observable<ApiResult<ArrayList<RoleInfoEntity>>> loadRoleInfo();

    @POST("/v2/kira/house/user/role/deskmate/{roleId}")
    Observable<ApiResult> setDeskMate(@Path("roleId") String roleId);

    @POST("/v2/kira/house/user/role/putInHouse/{roleId}")
    Observable<ApiResult> putInHouse(@Path("roleId") String roleId);

    @POST("/v2/kira/house/user/role/removeOutHouse/{roleId}")
    Observable<ApiResult> removeInHouse(@Path("roleId") String roleId);

    @GET("/v2/kira/house/user/tool/all")
    Observable<ApiResult<ArrayList<PropInfoEntity>>> loadPropInfo();

    @GET("/v2/kira/house/user/furniture/all")
    Observable<ApiResult<FurnitureInfoEntity>> loadFurnitureInfo();

    @POST("/v2/kira/house/visitor/save")
    Observable<ApiResult> saveVisitor(@Body SaveVisitorEntity entity);

    @GET("/v2/kira/house/visitor/page/{size}/{start}")
    Observable<ApiResult<ArrayList<VisitorsEntity>>> loadVisitor(@Path("size") int size, @Path("start") int start);


    @GET("v2/kira/house/inHouseObjects")
    Observable<ApiResult<ArrayList<MapEntity>>> loadHouseObjects();
}
