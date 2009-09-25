/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.felix.karaf.shell.osgi;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.karaf.shell.console.OsgiCommandSupport;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Option;
import org.osgi.framework.Bundle;

public abstract class BundlesCommand extends OsgiCommandSupport {

    @Argument(index = 0, name = "ids", description = "The list of bundle IDs separated by whitespaces", required = true, multiValued = true)
    List<Long> ids;

    @Option(name = "--force", aliases = {}, description = "Forces the command to execute", required = false, multiValued = false)
    boolean force;

    protected Object doExecute() throws Exception {
        List<Bundle> bundles = new ArrayList<Bundle>();
        if (ids != null && !ids.isEmpty()) {
            for (long id : ids) {
                Bundle bundle = getBundleContext().getBundle(id);
                if (bundle == null) {
                    System.err.println("Bundle ID" + id + " is invalid");
                } else {
                    if (force || !Util.isASystemBundle(getBundleContext(), bundle) || Util.accessToSystemBundleIsAllowed(bundle.getBundleId(), session)) {
                        bundles.add(bundle);
                    }
                }
            }
        }
        doExecute(bundles);
        return null;
    }

    protected abstract void doExecute(List<Bundle> bundles) throws Exception;
}
