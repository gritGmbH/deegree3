package org.deegree.sqldialect.postgis.function;

import org.deegree.sqldialect.SQLDialect;
import org.deegree.sqldialect.filter.expression.SQLExpression;
import org.deegree.sqldialect.filter.expression.SQLOperationBuilder;
import org.deegree.sqldialect.filter.function.SQLFunctionProvider;
import org.deegree.sqldialect.postgis.PostGISDialect;
import org.deegree.workspace.Workspace;

import java.util.List;

import static java.sql.Types.VARCHAR;

public class PostGISGeometryType implements SQLFunctionProvider {

	private static final String NAME = "geometryType";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public SQLExpression toProtoSQL(List<SQLExpression> args, SQLDialect dialect) {
		if (args.size() != 1) {
			throw new IllegalArgumentException(
					"Unable to map function '" + NAME + "' to SQL. Expected a single argument.");
		}

		boolean useLegacyPredicates = false;
		if (dialect instanceof PostGISDialect postGISDialect) {
			useLegacyPredicates = postGISDialect.isUseLegacyPredicates();
		}

		SQLOperationBuilder builder = new SQLOperationBuilder(VARCHAR);
		if (useLegacyPredicates) {
			builder.add("GeometryType(");
		}
		else {
			builder.add("substring( ST_GeometryType(");
		}
		builder.add(args.get(0));
		if (useLegacyPredicates) {
			builder.add(" )");
		}
		else {
			builder.add(" ) FROM 4 )");
		}
		return builder.toOperation();
	}

	@Override
	public void init(Workspace ws) {
		// nothing to do
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
