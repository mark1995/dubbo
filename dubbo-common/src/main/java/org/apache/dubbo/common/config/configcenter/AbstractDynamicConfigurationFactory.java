/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.common.config.configcenter;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.ConcurrentHashMapUtils;

import java.util.concurrent.ConcurrentHashMap;

import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_KEY;

/**
 * Abstract {@link DynamicConfigurationFactory} implementation with cache ability
 *
 * @see DynamicConfigurationFactory
 * @since 2.7.5
 */
public abstract class AbstractDynamicConfigurationFactory implements DynamicConfigurationFactory {

    // volatile 可以让 dynamicConfigurations 引用修改时，对其他线程可见
    // 如果确定引用不会被修改的话，可以指定为final 限制一下
    private volatile ConcurrentHashMap<String, DynamicConfiguration> dynamicConfigurations = new ConcurrentHashMap<>();

    @Override
    public final DynamicConfiguration getDynamicConfiguration(URL url) {
        String key = url == null ? DEFAULT_KEY : url.toServiceString();
        return ConcurrentHashMapUtils.computeIfAbsent(dynamicConfigurations, key, k -> createDynamicConfiguration(url));
    }

    protected abstract DynamicConfiguration createDynamicConfiguration(URL url);
}
