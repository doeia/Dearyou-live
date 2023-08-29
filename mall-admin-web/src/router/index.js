import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {path: '/login', component: () => import('../views/login/index'), hidden: true},
  {path: '/404', component: () => import('../views/404'), hidden: true},
  {
    path: '',
    component: Layout,
    redirect: '/home',
    children: [{
      path: 'home',
      name: 'home',
      component: () => import('../views/home/index'),
      meta: {title: '首页', icon: 'home'}
    }]
  }
]

export const asyncRouterMap = [
  {
    path: '/mms',
    component: Layout,
    redirect: '/mms/member',
    name: 'memberManage',
    meta: {title: '会员管理', icon: 'member'},
    children: [{
      path: 'member',
      name: 'memberList',
      component: () => import('../views/mms/member/index'),
      meta: {
        perms: ['ums:member:list'],
        title: '会员列表',
        icon: 'member'
      }
    }, {
      path: 'member/detail',
      name: 'memberDetail',
      component: () => import('../views/mms/member/detail'),
      meta: {
        perms: ['ums:member:read'],
        title: '会员详情'
      },
      hidden: true
    }, {
      path: 'member/update',
      name: 'memberUpdate',
      component: () => import('../views/mms/member/update'),
      meta: {
        perms: ['ums:member:update'],
        title: '修改会员'
      },
      hidden: true
    }, {
      path: 'member/add',
      name: 'memberAdd',
      component: () => import('../views/mms/member/add'),
      meta: {
        perms: ['ums:member:create'],
        title: '添加会员'
      },
      hidden: true
    }, {
      path: 'statistics',
      name: 'statistics',
      component: () => import('../views/mms/statistics/index'),
      meta: {
        title: '统计信息图表',
        icon: 'statistics'
      }
    }]
  },
  {
    path: '/pms',
    component: Layout,
    redirect: '/pms/product',
    name: 'pms',
    meta: {title: '商品管理', icon: 'product'},
    children: [{
      path: 'product',
      name: 'product',
      component: () => import('../views/pms/product/index'),
      meta: {
        perms: ['pms:product:list'],
        title: '商品列表',
        icon: 'product-list'
      }
    }, {
      path: 'productDetail',
      name: 'productDetail',
      component: () => import('../views/pms/product/detail'),
      meta: {
        perms: ['pms:product:read'],
        title: '商品详情'
      },
      hidden: true
    }, {
      path: 'addProduct',
      name: 'addProduct',
      component: () => import('../views/pms/product/add'),
      meta: {
        perms: ['pms:product:create'],
        title: '添加商品',
        icon: 'product-add'
      }
    }, {
      path: 'updateProduct',
      name: 'updateProduct',
      component: () => import('../views/pms/product/update'),
      meta: {
        perms: ['pms:product:update'],
        title: '修改商品',
        icon: 'product-add'
      },
      hidden: true
    }, {
      path: 'productCate',
      name: 'productCate',
      component: () => import('../views/pms/productCate/index'),
      meta: {
        perms: ['pms:productCategory:list'],
        title: '商品分类',
        icon: 'product-cate'
      }
    }, {
      path: 'addProductCate',
      name: 'addProductCate',
      component: () => import('../views/pms/productCate/add'),
      meta: {
        perms: ['pms:productCategory:create'],
        title: '添加商品分类'
      },
      hidden: true
    }, {
      path: 'updateProductCate',
      name: 'updateProductCate',
      component: () => import('../views/pms/productCate/update'),
      meta: {
        perms: ['pms:productCategory:update'],
        title: '修改商品分类'
      },
      hidden: true
    }, {
      path: 'productAttr',
      name: 'productAttr',
      component: () => import('../views/pms/productAttr/index'),
      meta: {
        perms: ['pms:productAttribute:list'],
        title: '商品属性',
        icon: 'product-attr'
      }
    }, {
      path: 'productAttrList',
      name: 'productAttrList',
      component: () => import('../views/pms/productAttr/productAttrList'),
      meta: {
        perms: ['pms:productAttribute:list'],
        title: '商品属性列表'
      },
      hidden: true
    }, {
      path: 'addProductAttr',
      name: 'addProductAttr',
      component: () => import('../views/pms/productAttr/addProductAttr'),
      meta: {
        perms: ['pms:productAttribute:create'],
        title: '添加商品属性'
      },
      hidden: true
    }, {
      path: 'updateProductAttr',
      name: 'updateProductAttr',
      component: () => import('../views/pms/productAttr/updateProductAttr'),
      meta: {
        perms: ['pms:productAttribute:update'],
        title: '修改商品属性'
      },
      hidden: true
    }, {
      path: 'brand',
      name: 'brand',
      component: () => import('../views/pms/brand/index'),
      meta: {
        perms: ['pms:brand:list'],
        title: '品牌管理',
        icon: 'product-brand'
      }
    }, {
      path: 'addBrand',
      name: 'addBrand',
      component: () => import('../views/pms/brand/add'),
      meta: {
        perms: ['pms:brand:create'],
        title: '添加品牌'
      },
      hidden: true
    }, {
      path: 'updateBrand',
      name: 'updateBrand',
      component: () => import('../views/pms/brand/update'),
      meta: {
        perms: ['pms:brand:update'],
        title: '编辑品牌'
      },
      hidden: true
    }, {
      path: 'brandCategory',
      name: 'brandCategoryList',
      component: () => import('../views/pms/brandCategory/index'),
      meta: {
        perms: ['pms:brandCategory:list'],
        title: '品牌分类管理',
        icon: 'product-brand'
      }
    }, {
      path: 'brandCategory/add',
      name: 'brandCategoryAdd',
      component: () => import('../views/pms/brandCategory/add'),
      meta: {
        perms: ['pms:brandCategory:create'],
        title: '添加品牌分类'
      },
      hidden: true
    }, {
      path: 'brandCategory/update',
      name: 'brandCategoryUpdate',
      component: () => import('../views/pms/brandCategory/update'),
      meta: {
        perms: ['pms:brandCategory:update'],
        title: '修改品牌分类'
      },
      hidden: true
    }, {
      path: 'feightTemplate',
      name: 'feightTemplate',
      component: () => import('../views/pms/feightTemplate/index'),
      meta: {
        perms: ['pms:feightTemplate:list'],
        title: '运费模板',
        icon: 'template'
      }
    }, {
      path: 'feightTemplate/add',
      name: 'feightTemplateAdd',
      component: () => import('../views/pms/feightTemplate/add'),
      meta: {
        perms: ['pms:feightTemplate:create'],
        title: '新增运费模版'
      },
      hidden: true
    }, {
      path: 'feightTemplate/update',
      name: 'feightTemplateUpdate',
      component: () => import('../views/pms/feightTemplate/update'),
      meta: {
        perms: ['pms:feightTemplate:update'],
        title: '修改运费模板'
      },
      hidden: true
    }, {
      path: 'comment',
      name: 'comment',
      component: () => import('../views/pms/comment/index'),
      meta: {
        perms: ['pms:comment:list'],
        title: '评论管理',
        icon: 'comment'
      }
    }, {
      path: 'replyComment',
      name: 'replyComment',
      component: () => import('../views/pms/comment/replyComment'),
      meta: {
        perms: ['pms:commentReplay:list'],
        title: '回复评论'
      },
      hidden: true
    }, {
      path: 'comment/detail',
      name: 'commentDetail',
      component: () => import('../views/pms/comment/commentDetail'),
      meta: {
        perms: ['pms:comment:read'],
        title: '评论详情'
      },
      hidden: true
    }]
  },
  {
    path: '/oms',
    component: Layout,
    redirect: '/oms/order',
    name: 'oms',
    meta: {title: '订单管理', icon: 'order'},
    children: [{
      path: 'order',
      name: 'order',
      component: () => import('../views/oms/order/index'),
      meta: {
        perms: ['oms:order:list'],
        title: '订单列表',
        icon: 'product-list'
      }
    }, {
      path: 'orderDetail',
      name: 'orderDetail',
      component: () => import('../views/oms/order/orderDetail'),
      meta: {
        perms: ['oms:order:read'],
        title: '订单详情'
      },
      hidden: true
    }, {
      path: 'deliverOrderList',
      name: 'deliverOrderList',
      component: () => import('../views/oms/order/deliverOrderList'),
      meta: {
        perms: ['oms:order:list'],
        title: '发货列表'
      },
      hidden: true
    }, {
      path: 'orderSetting',
      name: 'orderSetting',
      component: () => import('../views/oms/order/setting'),
      meta: {
        perms: ['oms:orderSetting:read', 'oms:orderSetting:update'],
        title: '订单设置',
        icon: 'order-setting'
      }
    }, {
      path: 'returnApply',
      name: 'returnApply',
      component: () => import('../views/oms/apply/index'),
      meta: {
        perms: ['oms:orderReturnApply:list'],
        title: '退货申请处理',
        icon: 'order-return'
      }
    }, {
      path: 'returnReason',
      name: 'returnReason',
      component: () => import('../views/oms/apply/reason'),
      meta: {
        perms: ['oms:orderReturnReason:list'],
        title: '退货原因设置',
        icon: 'order-return-reason'
      }
    }, {
      path: 'returnApplyDetail',
      name: 'returnApplyDetail',
      component: () => import('../views/oms/apply/applyDetail'),
      meta: {
        perms: ['oms:orderReturnApply:read'],
        title: '退货原因详情'
      },
      hidden: true
    }, {
      path: 'companyAddress',
      name: 'companyAddress',
      component: () => import('../views/oms/address/index'),
      meta: {
        perms: ['oms:companyAddress:list'],
        title: '发货地址管理',
        icon: 'address'
      }
    }, {
      path: 'companyAddress/add',
      name: 'companyAddressAdd',
      component: () => import('../views/oms/address/add'),
      meta: {
        perms: ['oms:companyAddress:create'],
        title: '新增地址管理'
      },
      hidden: true
    }, {
      path: 'companyAddress/update',
      name: 'companyAddressUpdate',
      component: () => import('../views/oms/address/update'),
      meta: {
        perms: ['oms:companyAddress:update'],
        title: '修改地址管理'
      },
      hidden: true
    }, {
      path: 'ship',
      name: 'ship',
      component: () => import('../views/oms/ship/index'),
      meta: {
        perms: ['oms:shipCompanies:list'],
        title: '快递公司列表',
        icon: 'delivery'
      }
    }, {
      path: 'ship/add',
      name: 'shipAdd',
      component: () => import('../views/oms/ship/add'),
      meta: {
        perms: ['oms:shipCompanies:create'],
        title: '新增快递公司'
      },
      hidden: true
    }, {
      path: 'ship/update',
      name: 'shipUpdate',
      component: () => import('../views/oms/ship/update'),
      meta: {
        perms: ['oms:shipCompanies:update'],
        title: '修改快递公司'
      },
      hidden: true
    }]
  },
  {
    path: '/sms',
    component: Layout,
    redirect: '/sms/coupon',
    name: 'sms',
    meta: {title: '营销管理', icon: 'sms'},
    children: [{
      path: 'coupon',
      name: 'coupon',
      component: () => import('../views/sms/coupon/index'),
      meta: {
        perms: ['sms:coupon:list'],
        title: '优惠券列表',
        icon: 'sms-coupon'
      }
    }, {
      path: 'addCoupon',
      name: 'addCoupon',
      component: () => import('../views/sms/coupon/add'),
      meta: {
        perms: ['sms:coupon:create'],
        title: '添加优惠券'
      },
      hidden: true
    }, {
      path: 'updateCoupon',
      name: 'updateCoupon',
      component: () => import('../views/sms/coupon/update'),
      meta: {
        perms: ['sms:coupon:update'],
        title: '修改优惠券'
      },
      hidden: true
    }, {
      path: 'couponHistory',
      name: 'couponHistory',
      component: () => import('../views/sms/coupon/history'),
      meta: {
        perms: ['sms:couponHistory:list'],
        title: '优惠券领取详情'
      },
      hidden: true
    }, {
      path: 'hot',
      name: 'homeHot',
      component: () => import('../views/sms/hot/index'),
      meta: {
        perms: ['sms:recommendProduct:list'],
        title: '人气推荐',
        icon: 'sms-hot'
      }
    }, {
      path: 'advertise',
      name: 'homeAdvertise',
      component: () => import('../views/sms/advertise/index'),
      meta: {
        perms: ['sms:advertise:list'],
        title: '广告列表',
        icon: 'sms-ad'
      }
    }, {
      path: 'addAdvertise',
      name: 'addHomeAdvertise',
      component: () => import('../views/sms/advertise/add'),
      meta: {
        perms: ['sms:advertise:create'],
        title: '添加广告'
      },
      hidden: true
    }, {
      path: 'updateAdvertise',
      name: 'updateHomeAdvertise',
      component: () => import('../views/sms/advertise/update'),
      meta: {
        perms: ['sms:advertise:update'],
        title: '编辑广告'
      },
      hidden: true
    }]
  },
  {
    path: '/cms',
    component: Layout,
    redirect: '/cms/announcement',
    name: 'contentManagement',
    meta: {title: '内容管理', icon: 'content'},
    children: [{
      path: 'announcement',
      name: 'announcementList',
      component: () => import('../views/cms/announcement/index'),
      meta: {
        perms: ['cms:announcement:list'],
        title: '公告',
        icon: 'announcement'
      }
    }, {
      path: 'announcement/add',
      name: 'announcementAdd',
      component: () => import('../views/cms/announcement/add'),
      meta: {
        perms: ['cms:announcement:create'],
        title: '添加公告'
      },
      hidden: true
    }, {
      path: 'announcement/update',
      name: 'announcementUpdate',
      component: () => import('../views/cms/announcement/update'),
      meta: {
        perms: ['cms:announcement:update'],
        title: '修改公告'
      },
      hidden: true
    }, {
      path: 'article',
      name: 'articleList',
      component: () => import('../views/cms/article/index'),
      meta: {
        perms: ['sms:article:list'],
        title: '彩妆知识', icon: 'article'
      }
    }, {
      path: 'article/add',
      name: 'articleAdd',
      component: () => import('../views/cms/article/add'),
      meta: {
        perms: ['sms:article:create'],
        title: '添加彩妆知识'
      },
      hidden: true
    }, {
      path: 'article/update',
      name: 'articleUpdate',
      component: () => import('../views/cms/article/update'),
      meta: {
        perms: ['sms:article:update'],
        title: '修改彩妆知识',
        icon: 'admin'
      },
      hidden: true
    }, {
      path: 'region',
      name: 'regionList',
      component: () => import('../views/cms/region/index'),
      meta: {
        perms: ['cms:regions:list'],
        title: '区域',
        icon: 'region'
      }
    }, {
      path: 'region/add',
      name: 'regionAdd',
      component: () => import('../views/cms/region/add'),
      meta: {
        perms: ['cms:regions:create'],
        title: '添加区域'
      },
      hidden: true
    }, {
      path: 'region/update',
      name: 'regionUpdate',
      component: () => import('../views/cms/region/update'),
      meta: {
        perms: ['cms:regions:update'],
        title: '修改区域'
      },
      hidden: true
    }]
  },
  {
    path: '/ums',
    component: Layout,
    redirect: '/ums/admin',
    name: 'userManage',
    meta: {title: '管理员管理', icon: 'admin'},
    children: [{
      path: 'admin',
      name: 'adminList',
      component: () => import('../views/ums/admin/index'),
      meta: {
        perms: ['ums:admin:list'],
        title: '管理员',
        icon: 'admin'
      }
    }, {
      path: 'admin/add',
      name: 'adminAdd',
      component: () => import('../views/ums/admin/add'),
      meta: {
        perms: ['ums:admin:create'],
        title: '添加管理员'
      },
      hidden: true
    }, {
      path: 'admin/update',
      name: 'adminUpdate',
      component: () => import('../views/ums/admin/update'),
      meta: {
        perms: ['ums:admin:update'],
        title: '修改管理员'
      },
      hidden: true
    }, {
      path: 'role',
      name: 'roleList',
      component: () => import('../views/ums/role/index'),
      meta: {
        perms: ['ums:role:list'],
        title: '角色',
        icon: 'role'
      }
    }, {
      path: 'permission',
      name: 'permissionList',
      component: () => import('../views/ums/permission/index'),
      meta: {
        perms: ['ums:permission:list'],
        title: '权限',
        icon: 'permission'
      }
    }, {
      path: 'syslog',
      name: 'syslog',
      component: () => import('../views/ums/log/index'),
      meta: {
        perms: ['ums:sysLog:list'],
        title: '日志',
        icon: 'syslog'
      }
    }, {
      path: 'syslog/detail',
      name: 'syslogDetail',
      component: () => import('../views/ums/log/detail'),
      meta: {
        perms: ['ums:sysLog:read'],
        title: '查看日志详情'
      },
      hidden: true
    }]
  },
  {path: '*', redirect: '/404', hidden: true}
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: [...constantRouterMap]
})
