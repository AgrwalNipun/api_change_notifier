package com.nipun.api_change_notifier.services;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;

@Service
public class ParsingService {

    final List<String> mappingNames = List.of("GetMapping", "PostMapping", "PutMapping", "DeleteMapping",
            "PatchMapping");

   public void parseForControllers(File file, Map<String, String> locations) {
    try {
        CompilationUnit parsed = StaticJavaParser.parse(file);

        parsed.findAll(ClassOrInterfaceDeclaration.class).forEach(clas -> {
            String basePath = clas.getAnnotationByName("RequestMapping")
                .flatMap(a -> a.toSingleMemberAnnotationExpr().map(s -> s.getMemberValue().toString()))
                .orElse("").replace("\"", "");

            for (String mappingName : mappingNames) {
                clas.getMethods().forEach(method -> {
                    method.getAnnotationByName(mappingName).ifPresent(methodAnnotations -> {
                        
                        // 1. Path Calculation
                        String methodPath = methodAnnotations.toSingleMemberAnnotationExpr()
                            .map(map -> map.getMemberValue().toString())
                            .orElse("").replace("\"", "");
                        String fullPath = basePath + methodPath;

                        System.out.println("\n--- Endpoint: [" + mappingName + "] " + fullPath + " ---");

                        // 2. Separate Parameters: @RequestBody vs @RequestParam/others
                        Map<String, Object> requestBodyMap = new HashMap<>();
                        Map<String, Object> queryParamsMap = new HashMap<>();

                        method.getParameters().forEach(param -> {
                            String typeName = resolveBaseType(param.getType().asString());
                            
                            if (param.isAnnotationPresent("RequestBody")) {
                                // This is the payload
                                requestBodyMap.putAll(parseDtosRecursively(typeName, locations, new HashSet<>()));
                            } else {
                                // This is likely a Query Param or Path Variable
                                if (locations.containsKey(typeName)) {
                                    queryParamsMap.put(param.getNameAsString(), parseDtosRecursively(typeName, locations, new HashSet<>()));
                                } else {
                                    queryParamsMap.put(param.getNameAsString(), typeName);
                                }
                            }
                        });

                        // 3. Resolve Return Type
                        String returnTypeStr = method.getType().asString();
                        String finalDtoName = (returnTypeStr.contains("<?>") || returnTypeStr.equals("ResponseEntity")) 
                                              ? resolveQuestionMark(method) 
                                              : resolveBaseType(returnTypeStr);

                        Map<String, Object> returnTypeMap = parseDtosRecursively(finalDtoName, locations, new HashSet<>());

                        // 4. Final Output
                        System.out.println("URL PARAMS   : " + queryParamsMap);
                        System.out.println("REQUEST BODY : " + requestBodyMap);
                        System.out.println("RETURN TYPE  : " + returnTypeMap);
                    });
                });
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}


public void parseForControllers2(File file, Map<String, String> locations) {
    try {
        CompilationUnit parsed = StaticJavaParser.parse(file);

        parsed.findAll(ClassOrInterfaceDeclaration.class).forEach(clas -> {
            String basePath = clas.getAnnotationByName("RequestMapping")
                .flatMap(a -> a.toSingleMemberAnnotationExpr().map(s -> s.getMemberValue().toString()))
                .orElse("").replace("\"", "");

            for (String mappingName : mappingNames) {
                clas.getMethods().forEach(method -> {
                    method.getAnnotationByName(mappingName).ifPresent(methodAnnotations -> {
                        
                        // 1. Path Calculation
                        String methodPath = methodAnnotations.toSingleMemberAnnotationExpr()
                            .map(map -> map.getMemberValue().toString())
                            .orElse("").replace("\"", "");
                        String fullPath = basePath + methodPath;

                        System.out.println("\n--- Endpoint: [" + mappingName + "] " + fullPath + " ---");

                        // 2. Separate Parameters: @RequestBody vs @RequestParam/others
                        Map<String, Object> requestBodyMap = new HashMap<>();
                        Map<String, Object> queryParamsMap = new HashMap<>();

                        method.getParameters().forEach(param -> {
                            String typeName = resolveBaseType(param.getType().asString());
                            
                            if (param.isAnnotationPresent("RequestBody")) {
                                // This is the payload
                                requestBodyMap.putAll(parseDtosRecursively(typeName, locations, new HashSet<>()));
                            } else {
                                // This is likely a Query Param or Path Variable
                                if (locations.containsKey(typeName)) {
                                    queryParamsMap.put(param.getNameAsString(), parseDtosRecursively(typeName, locations, new HashSet<>()));
                                } else {
                                    queryParamsMap.put(param.getNameAsString(), typeName);
                                }
                            }
                        });

                        // 3. Resolve Return Type
                        String returnTypeStr = method.getType().asString();
                        String finalDtoName = (returnTypeStr.contains("<?>") || returnTypeStr.equals("ResponseEntity")) 
                                              ? resolveQuestionMark(method) 
                                              : resolveBaseType(returnTypeStr);

                        Map<String, Object> returnTypeMap = parseDtosRecursively(finalDtoName, locations, new HashSet<>());

                        // 4. Final Output
                        System.out.println("URL PARAMS   : " + queryParamsMap);
                        System.out.println("REQUEST BODY : " + requestBodyMap);
                        System.out.println("RETURN TYPE  : " + returnTypeMap);
                    });
                });
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private String resolveQuestionMark(com.github.javaparser.ast.body.MethodDeclaration method) {
        return method.getBody().map(body -> {
            List<com.github.javaparser.ast.stmt.ReturnStmt> returns = body
                    .findAll(com.github.javaparser.ast.stmt.ReturnStmt.class);

            for (com.github.javaparser.ast.stmt.ReturnStmt ret : returns) {
                if (ret.getExpression().isEmpty())
                    continue;
                var expr = ret.getExpression().get();

                // 1. Handle: return ResponseEntity.ok(userDto); OR return
                // ResponseEntity.ok().body(userDto);
                if (expr.isMethodCallExpr()) {
                    String type = resolveFromMethodCall(body, expr.asMethodCallExpr());
                    if (!type.equals("?") && !type.equals("Void"))
                        return type;
                }

                // 2. Handle: return new ResponseEntity<>(userDto, HttpStatus.OK);
                if (expr.isObjectCreationExpr()) {
                    var creation = expr.asObjectCreationExpr();
                    if (!creation.getArguments().isEmpty()) {
                        String argName = creation.getArgument(0).toString();
                        String type = findTypeInBody(body, argName);
                        if (!type.equals("?"))
                            return type;
                    }
                }
            }
            return "?";
        }).orElse("?");
    }

    private String resolveFromMethodCall(com.github.javaparser.ast.stmt.BlockStmt body,
            com.github.javaparser.ast.expr.MethodCallExpr call) {
        if (!call.getArguments().isEmpty()) {
            var firstArg = call.getArgument(0);
            if (firstArg.isNameExpr()) {
                return findTypeInBody(body, firstArg.asNameExpr().getNameAsString());
            }
            if (firstArg.isObjectCreationExpr()) {
                return firstArg.asObjectCreationExpr().getTypeAsString();
            }
        }
        if (call.getScope().isPresent() && call.getScope().get().isMethodCallExpr()) {
            return resolveFromMethodCall(body, call.getScope().get().asMethodCallExpr());
        }

        return "?";
    }

    private String findTypeInBody(com.github.javaparser.ast.stmt.BlockStmt body, String varName) {
        return body.findAll(com.github.javaparser.ast.body.VariableDeclarator.class).stream()
                .filter(vd -> vd.getNameAsString().equals(varName))
                .map(vd -> vd.getTypeAsString())
                .findFirst()
                .orElse("?");
    }

    /**
     * Recursively resolves complex types to find the base DTO.
     * Handles: ResponseEntity<List<UserDTO>> -> UserDTO
     * Handles: Map<String, UserDTO> -> UserDTO
     */
    private String resolveBaseType(String returnType) {
        if (!returnType.contains("<")) {
            return returnType;
        }

        try {
            com.github.javaparser.ast.type.Type parsedType = StaticJavaParser.parseType(returnType);

            if (parsedType.isClassOrInterfaceType()) {
                var cit = parsedType.asClassOrInterfaceType();

                if (cit.getTypeArguments().isPresent()) {

                    var args = cit.getTypeArguments().get();

                    com.github.javaparser.ast.type.Type targetArg = args.get(args.size() - 1);

                    String targetName = targetArg.asString();

                    return resolveBaseType(targetName);
                }
            }
        } catch (Exception e) {
            // Manual efforts if cant parse
            int start = returnType.lastIndexOf("<") + 1;
            int end = returnType.indexOf(">");
            if (start > 0 && end > start) {
                return resolveBaseType(returnType.substring(start, end));
            }
        }
        return returnType;
    }

 

private Map<String, Object> parseDtosRecursively(String className, Map<String, String> locations, Set<String> visited) {
    Map<String, Object> currentClassFields = new HashMap<>();

    // If we don't have the source code, we can't parse it
    String path = locations.get(className);
    if (path == null) return currentClassFields;

    // Prevent infinite recursion in the same branch
    if (visited.contains(className)) return currentClassFields;
    visited.add(className);

    try {
        CompilationUnit cu = StaticJavaParser.parse(new File(path));
        cu.getClassByName(className).ifPresent(clazz -> {
            
            // 1. Process Fields (Composition)
            clazz.getFields().forEach(field -> {
                field.getVariables().forEach(var -> {
                    String fieldName = var.getNameAsString();
                    String fieldType = var.getTypeAsString();
                    String resolvedType = resolveBaseType(fieldType);

                    if (locations.containsKey(resolvedType)) {
                        // Create a NEW visited set for the child call to allow 
                        // the same DTO to appear in different branches
                        currentClassFields.put(fieldName, parseDtosRecursively(resolvedType, locations, new HashSet<>(visited)));
                    } else {
                        currentClassFields.put(fieldName, fieldType);
                    }
                });
            });

            // 2. Process Inheritance
            clazz.getExtendedTypes().forEach(extendedType -> {
                currentClassFields.putAll(parseDtosRecursively(extendedType.getNameAsString(), locations, visited));
            });
        });
    } catch (Exception e) {
        System.err.println("Error reading " + className + ": " + e.getMessage());
    }
    return currentClassFields;
}
    
}
